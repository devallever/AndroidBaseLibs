package app.allever.android.lib.core.function.camera

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.hardware.Camera
import android.util.Log
import android.view.Surface
import android.view.WindowManager

/**
 * Created by Allever on 18/5/11.
 */
@Deprecated("")
object CameraUtil {
    private const val TAG = "CameraUtil"
    /**
     * 打印支持的pictureSizes
     *
     * @param parameters
     */
    fun printSupportPictureSize(parameters: Camera.Parameters?) {
        if (parameters == null) {
            return
        }
        val pictureSizes = parameters.supportedPictureSizes
        for (size in pictureSizes) {
            Log.d(TAG, "pictureSizes:width = " + size.width + " height = " + size.height)
        }
    }

    /**
     * 打印支持的previewSizes
     *
     * @param params
     */
    fun printSupportPreviewSize(params: Camera.Parameters?) {
        if (params == null) {
            return
        }
        val previewSizes = params.supportedPreviewSizes
        for (size in previewSizes) {
            Log.d(TAG, "previewSizes:width = " + size.width + " height = " + size.height)
        }
    }

    /**
     * 打印支持的聚焦模式
     *
     * @param parameters
     */
    fun printSupportFocusMode(parameters: Camera.Parameters?) {
        if (parameters == null) {
            return
        }
        val focusModes = parameters.supportedFocusModes
        for (mode in focusModes) {
            Log.d(TAG, "focusModes--$mode")
        }
    }

    /**
     * 获取相机拍照图片大小 */
    fun getPropPictureSize(parameters: Camera.Parameters?, maxHeight: Int): Camera.Size? {
        if (parameters == null) {
            return null
        }
        val pictureSizes = parameters.supportedPictureSizes
        var i = 0
        while (i < pictureSizes.size) {
            val size = pictureSizes[i]
            Log.d(TAG, "getPropPictureSize: width = " + size.width + " height = " + size.height + " maxHeight = " + maxHeight)
            if (size.height <= maxHeight) {
                break
            }
            i++
        }
        return pictureSizes[i]
    }

    /**
     * 获取相机预览大小
     */
    fun getPropPreviewSize(parameters: Camera.Parameters?, maxHeight: Int): Camera.Size? {
        if (parameters == null) {
            return null
        }
        val previewSizes = parameters.supportedPreviewSizes
        var i = 0
        while (i < previewSizes.size) {
            val size = previewSizes[i]
            if (size.height <= maxHeight) {
                break
            }
            i++
        }
        return previewSizes[i]
    }

    /** Check if this device has a camera  */
    fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    fun getCameraId(front: Boolean): Int {
        return if (front) {
            Camera.CameraInfo.CAMERA_FACING_FRONT
        } else {
            Camera.CameraInfo.CAMERA_FACING_BACK
        }
    }

    /**
     * 获取cameraId 前置或后置摄像头，下标0开始计算
     * @param tagInfo Camera.CameraInfo.CAMERA_FACING_BACK 或 CAMERA_FACING_FRONT
     */
    fun getCameraInfoId(tagInfo: Int): Int {
        Log.d(TAG, "getCameraInfoId: ")
        val cameraInfo = Camera.CameraInfo()
        // 开始遍历摄像头，得到camera info
        val cameraCount = Camera.getNumberOfCameras()
        Log.d(TAG, "getCameraInfoId: cameraCount = $cameraCount")
        var cameraId = 0
        while (cameraId < cameraCount) {
            Camera.getCameraInfo(cameraId, cameraInfo)
            if (cameraInfo.facing == tagInfo) {
                break
            }
            cameraId++
        }
        Log.d(TAG, "getCameraInfoId: cameraId = $cameraId")
        return cameraId
    }


    fun getRotationOnTakePickPic(context: Context?, cameraId: Int): Int {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(cameraId, info)
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        val rotation = windowManager?.defaultDisplay?.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }
        return if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            (info.orientation - degrees + 360) % 360
        } else {  // back-facing camera
            (info.orientation + degrees) % 360
        }
    }

    fun getCameraRotationDegreeOnPreview(context: Context?, cameraId: Int): Int {
        val info = Camera.CameraInfo()
        Log.d(TAG, "getCameraRotationDegreeOnPreview: cameraId = $cameraId")
        Camera.getCameraInfo(cameraId, info)
        val windowManager = context?.getSystemService(Context.WINDOW_SERVICE) as? WindowManager
        val rotation = windowManager?.defaultDisplay?.rotation
        var degrees = 0
        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }
        var result: Int
        //前置
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360
            result = (360 - result) % 360
        } else {
            result = (info.orientation - degrees + 360) % 360
        }//后置

        return result
    }

    /**
     * 获取旋转后的图片
     * @param b
     * @param rotateDegree 旋转角度
     * @return
     */
    fun getRotateBitmap(b: Bitmap?, rotateDegree: Float): Bitmap? {
        if (b == null) {
            return null
        }
        val matrix = Matrix()
        matrix.postRotate(rotateDegree)
        return Bitmap.createBitmap(b, 0, 0, b.width, b.height, matrix, false)
    }
}
