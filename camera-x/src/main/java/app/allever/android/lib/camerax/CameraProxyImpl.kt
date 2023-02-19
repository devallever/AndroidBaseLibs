package app.allever.android.lib.camerax

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Point
import android.hardware.Camera
import android.hardware.Camera.CameraInfo
import android.view.Surface
import android.view.SurfaceView
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.LifecycleOwner
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.logE
import app.allever.android.lib.core.function.camera.*
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.Executors
import kotlin.math.abs

class CameraProxyImpl : ICameraProxy {

    private val mExecutor = Executors.newSingleThreadExecutor()

    private var mCamera: Camera? = null

    private var mListener: CameraListener? = null

    /**
     * 相机id， 默认使用后置
     */
    private var mCameraId = CameraInfo.CAMERA_FACING_BACK

    private lateinit var mPreviewRef: WeakReference<View>

    /**
     * 额外的旋转角度（用于适配一些定制设备）
     */
    private var additionalRotation = 0

    private var previewSize: Size? = null

    private val mPreviewCallback = Camera.PreviewCallback { data, camera ->
//        log("onPreviewFrame")
        val format = camera?.parameters?.previewFormat
        mListener?.onPreview(data, format!!)
    }

    private val mPictureCallback = Camera.PictureCallback { data, camera ->
        if (data == null || mListener == null) {
            return@PictureCallback
        }

        mExecutor.execute {
            var additionalDegree = 0
            if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                additionalDegree = 180
            }
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            val degree: Int =
                getDisplayOrientation(mPreviewRef.get()?.context, mCameraId) + additionalDegree

            val rotateBitmap: Bitmap? = CameraManager.getRotateBitmap(bitmap, degree.toFloat())
            mListener?.onTakePicture(data, rotateBitmap, mCamera?.parameters?.pictureFormat!!)
            closeCamera()
            openCamera(mCameraId)
        }
    }

    override fun openCamera() {
        if (mCamera != null) {
            return
        }

        //获取合适的cameraId 后置->前置
        val cameraFacing = getCameraFacing()
        openCamera(cameraFacing)
    }

    override fun openCamera(@CameraFacing.STATE cameraFacing: Int) {
        if (mCamera != null) {
            return
        }
        mCameraId = when (cameraFacing) {
            CameraFacing.FACE_BACK -> CameraInfo.CAMERA_FACING_BACK
            else -> CameraInfo.CAMERA_FACING_FRONT
        }
        val cameraIdList = CameraManager.getCameraIdList(mPreviewRef.get()?.context)
        val cameraCount = cameraIdList.size
        log("相机数 = $cameraCount")
        cameraIdList.forEach {
            log("相机id：$it")
        }
        log("打开相机：$mCameraId")
        try {
            if (mCamera == null) {
                mCamera = Camera.open(mCameraId)
            }

            if (mCamera == null) {
                logE("打开相机失败")
                return
            }

            val camera = mCamera!!

            //获取参数
            val params = camera.parameters

            //最佳大小
            previewSize = Size(1920, 1080)
            val bestSupportSize =
                getBestSupportedSize(params.supportedPreviewSizes, Point(1920, 1080))
            log("最佳大小 = ${bestSupportSize.width} x ${bestSupportSize.height}")
            params.setPreviewSize(bestSupportSize.width, bestSupportSize.height)
            params.setPictureSize(bestSupportSize.width, bestSupportSize.height)

            //设置预览格式
            params.previewFormat = ImageFormat.NV21
            //设置预览大小
            val previewSize = params.previewSize
            log("当前预览大小：${previewSize.width} x ${previewSize.height}")
            getSupportPreviewSize()

            //设置图片大小
            val pictureSize = params.pictureSize
            log("当前图片大小：${pictureSize.width} x ${pictureSize.height}")
            getSupportPictureSize()

            //设置自动对焦
            val supportedFocusModes: List<String> = params.supportedFocusModes
            if (supportedFocusModes.isNotEmpty()) {
                when {
                    supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) -> {
                        params.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
                    }
                    supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO) -> {
                        params.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO
                    }
                    supportedFocusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO) -> {
                        params.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
                    }
                }
            }


            camera.parameters = params

            when (mPreviewRef.get()) {
                is SurfaceView -> {
                    camera.setPreviewDisplay((mPreviewRef.get() as SurfaceView).holder)
                }
            }

            camera.setDisplayOrientation(
                getDisplayOrientation(
                    mPreviewRef.get()?.context,
                    mCameraId
                )
            )
            camera.setPreviewCallback(mPreviewCallback)
            camera.startPreview()
        } catch (e: Exception) {
            e.printStackTrace()
            mListener?.onError(e.message ?: "打开相机失败")
        }
    }

    override fun closeCamera() {
        synchronized(this) {
            if (mCamera == null) {
                return
            }
            mCamera?.setPreviewCallback(null)
            mCamera?.stopPreview()
            mCamera?.release()
            mCamera = null
            log("关闭相机")
        }
    }

    override fun release() {
        mCamera = null
    }

    override fun setPreview(view: View) {
        mPreviewRef = WeakReference(view)
    }

    override fun setLifeCycleOwner(lifecycleOwner: LifecycleOwner) {

    }

    override fun takePicture() {
        mCamera?.takePicture(null, null, mPictureCallback)
    }

    override fun startRecordVideo() {

    }

    override fun stopRecordVideo() {

    }

    override fun setCameraListener(listener: CameraListener?) {
        mListener = listener
    }

    override fun getDisplayOrientation(context: Context?, cameraId: Int): Int {
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        val rotation = windowManager?.defaultDisplay?.rotation
        var degrees = rotation!! * 90
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
            else -> {
            }
        }
        additionalRotation /= 90
        additionalRotation *= 90
        degrees += additionalRotation
        var result: Int
        val info = CameraInfo()
        Camera.getCameraInfo(mCameraId, info)
        if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360
            result = (360 - result) % 360
        } else {
            result = (info.orientation - degrees + 360) % 360
        }
        return result
    }

    override fun getSupportPreviewSize(): MutableList<Size> {
        val sizeList = getSizeList(mCamera?.parameters?.supportedPreviewSizes)
        sizeList.forEach {
            log("cameraId : $mCameraId 支持预览大小 = ${it.width} x ${it.height}")
        }
        return sizeList
    }

    override fun getSupportPictureSize(): MutableList<Size> {
        val sizeList = getSizeList(mCamera?.parameters?.supportedPictureSizes)
        sizeList.forEach {
            log("cameraId : $mCameraId 支持图片大小 = ${it.width} x ${it.height}")
        }
        return sizeList
    }

    private fun getSizeList(supportSizeList: List<Camera.Size>?): MutableList<Size> {
        val sizeList = mutableListOf<Size>()
        supportSizeList?.forEach { supportSize ->
            val size = Size(supportSize.width, supportSize.height)
            sizeList.add(size)
        }
        return sizeList
    }

    private fun getCameraFacing(): Int {
        return CameraFacing.FACE_BACK
    }

    private fun getBestSupportedSize(
        sizes: List<Camera.Size>,
        previewViewSize: Point?
    ): Camera.Size {
        var sizes: List<Camera.Size>? = sizes
        if (sizes == null || sizes.isEmpty()) {
            return mCamera!!.parameters.previewSize
        }
        val tempSizes =
            sizes.toTypedArray()
        Arrays.sort(
            tempSizes
        ) { o1, o2 ->
            if (o1.width > o2.width) {
                -1
            } else if (o1.width == o2.width) {
                if (o1.height > o2.height) -1 else 1
            } else {
                1
            }
        }
        sizes = listOf(*tempSizes)
        var bestSize = sizes[0]
        var previewViewRatio: Float
        previewViewRatio = if (previewViewSize != null) {
            previewViewSize.x.toFloat() / previewViewSize.y.toFloat()
        } else {
            bestSize.width.toFloat() / bestSize.height.toFloat()
        }
        if (previewViewRatio > 1) {
            previewViewRatio = 1 / previewViewRatio
        }
        val isNormalRotate = additionalRotation % 180 == 0
        for (s in sizes) {
            if (previewSize != null && previewSize?.width == s.width && previewSize?.height == s.height) {
                return s
            }
            if (isNormalRotate) {
                if (abs(s.height / s.width.toFloat() - previewViewRatio) < abs(
                        bestSize.height / bestSize.width.toFloat() - previewViewRatio
                    )
                ) {
                    bestSize = s
                }
            } else {
                if (abs(s.width / s.height.toFloat() - previewViewRatio) < abs(
                        bestSize.width / bestSize.height.toFloat() - previewViewRatio
                    )
                ) {
                    bestSize = s
                }
            }
        }
        return bestSize
    }

}