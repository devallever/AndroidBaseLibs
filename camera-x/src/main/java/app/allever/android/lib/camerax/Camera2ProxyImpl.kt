package app.allever.android.lib.camerax

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.hardware.camera2.*
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.logE
import app.allever.android.lib.core.function.camera.*
import app.allever.android.lib.core.function.camera.CameraManager
import app.allever.android.lib.core.function.camera.util.getPreviewOutputSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class Camera2ProxyImpl : ICameraProxy {

    private val IMAGE_BUFFER_SIZE = 3

    private val mCoroutine = CoroutineScope(Dispatchers.Main)

    private val mCameraManager by lazy {
        App.context.getSystemService(Context.CAMERA_SERVICE) as android.hardware.camera2.CameraManager
    }

    private lateinit var mCharacteristics: CameraCharacteristics

    /**
     * 默认使用后置摄像头吧
     */
    private var mCameraId = 0

    /**
     * 预览View
     */
    private lateinit var mPreviewRef: WeakReference<SurfaceView>

    /**
     * 用来打开关闭相机
     */
    private var mCamera: CameraDevice? = null

    private lateinit var mCameraSession: CameraCaptureSession

    /**
     * 读取图片数据
     */
    private lateinit var mImageReader: ImageReader

    private val mCameraThread = HandlerThread("CameraThread").apply { start() }

    private val mCameraHandler = Handler(mCameraThread.looper)

    private val mImageReaderThread = HandlerThread("imageReaderThread").apply { start() }

    private val mImageReaderHandler = Handler(mImageReaderThread.looper)

    private var mListener: CameraListener? = null

    private var mCameraFacing = 0;

    override fun openCamera() {
        mCharacteristics = mCameraManager.getCameraCharacteristics("0")
        mCameraId = 0;
        openCamera(CameraFacing.FACE_BACK)
    }

    override fun openCamera(cameraFacing: Int) {
        mCameraFacing = cameraFacing
        mCoroutine.launch(Dispatchers.Main) {
            if (cameraFacing == CameraFacing.FACE_BACK) {
                mCharacteristics = mCameraManager.getCameraCharacteristics("0")
                mCameraId = 0;
            } else {
                mCharacteristics = mCameraManager.getCameraCharacteristics("1")
                mCameraId = 1;
            }


            // Selects appropriate preview size and configures view finder
            val previewSize = getPreviewOutputSize(
                mPreviewRef.get()!!.display, mCharacteristics, SurfaceHolder::class.java
            )
            log("View finder size: ${mPreviewRef.get()!!.width} x ${mPreviewRef.get()!!.height}")
            log("Selected preview size: $previewSize")

            if (mPreviewRef.get() is AutoFitSurfaceView) {
                (mPreviewRef.get() as AutoFitSurfaceView).setAspectRatio(
                    previewSize.width,
                    previewSize.height
                )
            }

            if (ActivityCompat.checkSelfPermission(
                    App.context,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                mListener?.onError("没有相机权限")
                return@launch
            }

            mCamera = openCameraInternal()
            val configMap = mCharacteristics.get(
                CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
            )!!
//            val size = android.util.Size(1920, 1080);
            val sizeArr = configMap.getOutputSizes(ImageFormat.JPEG)
            sizeArr.map {
                log("size = ${it.width} x ${it.height}")
            }
            val size =
                configMap.getOutputSizes(ImageFormat.JPEG)?.maxByOrNull { it.height * it.width }!!
            mImageReader = ImageReader.newInstance(
                size.width, size.height, ImageFormat.JPEG, IMAGE_BUFFER_SIZE
            )

            // Creates list of Surfaces where the camera will output frames
            val targets = listOf(mPreviewRef.get()?.holder?.surface, mImageReader.surface)

            // Start a capture session using our open camera and list of Surfaces where frames will go
            mCameraSession =
                createCaptureSession(mCamera!!, targets as List<Surface>, mCameraHandler)

            val captureRequest = mCamera?.createCaptureRequest(
                CameraDevice.TEMPLATE_PREVIEW
            )?.apply { addTarget(mPreviewRef.get()?.holder?.surface!!) }

            // This will keep sending the capture request as frequently as possible until the
            // session is torn down or session.stopRepeating() is called
            mCameraSession.setRepeatingRequest(captureRequest?.build()!!, null, mCameraHandler)
        }

    }

    override fun closeCamera() {
        mCamera?.close()
    }

    override fun release() {
        closeCamera()
    }

    override fun setPreview(view: View) {
        if (view is SurfaceView) {
            mPreviewRef = WeakReference(view)
        } else {
            throw RuntimeException("必须设置SurfaceView")
        }
    }

    override fun setLifeCycleOwner(lifecycleOwner: LifecycleOwner) {
    }

    override fun takePicture() {
        mImageReader.setOnImageAvailableListener({ reader ->
            log("onImageAvailable")
            val image = reader.acquireLatestImage()
            val buffer = image.planes[0].buffer
            val bytes = ByteArray(buffer.remaining()).apply { buffer.get(this) }
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            val degree = when (mCameraFacing) {
                CameraFacing.FACE_BACK -> 90f
                else -> 270f
            }
            val rotateBitmap = CameraManager.getRotateBitmap(bitmap, degree)
            mListener?.onTakePicture(bytes, rotateBitmap, mImageReader.imageFormat)

        }, mImageReaderHandler)

        val captureRequest = mCameraSession.device.createCaptureRequest(
            CameraDevice.TEMPLATE_STILL_CAPTURE
        ).apply {
            addTarget(mImageReader.surface)
        }

        mCameraSession.capture(
            captureRequest.build(),
            object : CameraCaptureSession.CaptureCallback() {
                override fun onCaptureCompleted(
                    session: CameraCaptureSession,
                    request: CaptureRequest,
                    result: TotalCaptureResult
                ) {
                    super.onCaptureCompleted(session, request, result)
                    log("onCaptureCompleted")
                }
            },
            mImageReaderHandler
        )

    }

    override fun setCameraListener(listener: CameraListener?) {
        mListener = listener
    }

    override fun getDisplayOrientation(context: Context?, cameraId: Int): Int {
        return 0
    }

    override fun getSupportPreviewSize(): MutableList<Size> {
        return mutableListOf()
    }

    override fun getSupportPictureSize(): MutableList<Size> {
        return mutableListOf()
    }

    private suspend fun openCameraInternal(): CameraDevice = suspendCoroutine {
        if (ActivityCompat.checkSelfPermission(
                App.context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return@suspendCoroutine
        }
        mCameraManager.openCamera("$mCameraId", object : CameraDevice.StateCallback() {
            override fun onOpened(camera: CameraDevice) {
                logE("打开相机成功")
                it.resume(camera)
            }

            override fun onDisconnected(camera: CameraDevice) {
            }

            override fun onError(camera: CameraDevice, error: Int) {
                logE("打开相机错误： $error")
                mListener?.onError("打开相机错误： $error")
            }
        }, mCameraHandler)
    }

    /**
     * Starts a [CameraCaptureSession] and returns the configured session (as the result of the
     * suspend coroutine
     */
    private suspend fun createCaptureSession(
        device: CameraDevice,
        targets: List<Surface>,
        handler: Handler? = null
    ): CameraCaptureSession = suspendCoroutine { cont ->

        // Create a capture session using the predefined targets; this also involves defining the
        // session state callback to be notified of when the session is ready
        device.createCaptureSession(targets, object : CameraCaptureSession.StateCallback() {

            override fun onConfigured(session: CameraCaptureSession) = cont.resume(session)

            override fun onConfigureFailed(session: CameraCaptureSession) {
                val exc = RuntimeException("Camera ${device.id} session configuration failed")
                logE(exc.message ?: "")
                cont.resumeWithException(exc)
            }
        }, handler)
    }
}