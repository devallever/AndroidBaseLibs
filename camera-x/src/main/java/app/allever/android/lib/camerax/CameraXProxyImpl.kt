package app.allever.android.lib.camerax

import android.content.Context
import android.graphics.BitmapFactory
import android.view.View
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.function.camera.*
import com.google.common.util.concurrent.ListenableFuture
import java.io.File
import java.lang.ref.WeakReference
import java.util.concurrent.Executors

class CameraXProxyImpl : ICameraProxy {

    private val mExecutor = Executors.newCachedThreadPool()

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    private lateinit var mPreviewRef: WeakReference<PreviewView>

    private lateinit var mLifecycleOwner: LifecycleOwner

    private var mCameraListener: CameraListener? = null

    private var mCameraFacing = CameraFacing.FACE_BACK

    /**
     * 默认打开后置
     */
    private var mCameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    private lateinit var mImageAnalysis: ImageAnalysis

    private lateinit var mImageCapture: ImageCapture


    private var mDisplayId = -1

    private val mImageAnalysisCallback = ImageAnalysis.Analyzer { image ->
        mCameraListener?.onPreview(ByteArray(0), image.format)
        image.close()
    }


    private val mCameraRunnable = Runnable {
        val cameraProvider = cameraProviderFuture.get()
        val preview = Preview.Builder().build()
        try {
            cameraProvider.unbindAll()

            mImageAnalysis = ImageAnalysis.Builder()
                .setTargetRotation(rotation)
                .build()
            mImageCapture = ImageCapture.Builder()
                .setTargetRotation(rotation)
                .build()

            val camera = cameraProvider.bindToLifecycle(
                mLifecycleOwner,
                mCameraSelector,
                preview,
                mImageAnalysis,
                mImageCapture
            )
            mImageAnalysis.setAnalyzer(mExecutor, mImageAnalysisCallback)
            preview.setSurfaceProvider(mPreviewRef.get()?.surfaceProvider)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private val mCaptureCallback = object : ImageCapture.OnImageSavedCallback {

        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
            log("拍照保存成功")
            val photoFile = File(mPreviewRef.get()?.context?.cacheDir, "camerax_capture_temp")
            val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
            val degree = when(mCameraFacing) {
                CameraFacing.FACE_BACK -> 90f
                else -> 270f
            }
            val rotateBitmap = CameraManager.getRotateBitmap(bitmap, degree)
            photoFile.delete()
            mCameraListener?.onTakePicture(null, rotateBitmap, 0)
        }

        override fun onError(exception: ImageCaptureException) {
            log("拍照保存失败: ${exception.message}")
        }
    }

    override fun openCamera() {
        openCamera(CameraFacing.FACE_BACK)
    }

    override fun openCamera(@CameraFacing.STATE cameraFacing: Int) {
        mCameraFacing = cameraFacing
        try {
            mCameraSelector = when (cameraFacing) {
                CameraFacing.FACE_BACK -> CameraSelector.DEFAULT_BACK_CAMERA
                else -> CameraSelector.DEFAULT_FRONT_CAMERA
            }

            cameraProviderFuture = ProcessCameraProvider.getInstance(mPreviewRef.get()?.context!!)
            cameraProviderFuture.addListener(
                mCameraRunnable,
                ContextCompat.getMainExecutor(mPreviewRef.get()?.context!!)
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun closeCamera() {

    }

    override fun release() {
    }

    private var rotation = 0
    override fun setPreview(view: View) {
        if (view is PreviewView) {
            mPreviewRef = WeakReference(view)
            rotation = mPreviewRef.get()?.display?.rotation ?: 0
        } else {
            throw RuntimeException("必须设置PreviewView")
        }
    }

    override fun setLifeCycleOwner(lifecycleOwner: LifecycleOwner) {
        mLifecycleOwner = lifecycleOwner
    }

    override fun takePicture() {
        // Create output file to hold the image
        val photoFile = File(mPreviewRef.get()?.context?.cacheDir, "camerax_capture_temp")

        // Setup image capture metadata
        val metadata = ImageCapture.Metadata().apply {

            // Mirror image when using the front camera
            isReversedHorizontal = mCameraFacing == CameraSelector.LENS_FACING_FRONT
        }

        // Create output options object which contains file + metadata
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile)
            .setMetadata(metadata)
            .build()
        mImageCapture.takePicture(outputOptions, mExecutor, mCaptureCallback)
    }

    override fun setCameraListener(listener: CameraListener?) {
        mCameraListener = listener
    }

    override fun getDisplayOrientation(context: Context?, cameraId: Int): Int {
        return 0
    }

    override fun getSupportPreviewSize(): MutableList<Size> {
        return mutableListOf<Size>()
    }

    override fun getSupportPictureSize(): MutableList<Size> {
        return mutableListOf<Size>()
    }
}