package app.allever.android.lib.core.function.camera

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner

interface ICameraProxy {

    /**
     * 打开相机
     */
    fun openCamera()

    /**
     * 打开指定相机
     * @param cameraId
     */
    fun openCamera(@CameraFacing.STATE cameraFacing: Int)

    /**
     * 关闭相机
     */
    fun closeCamera()

    /**
     * 释放资源
     */
    fun release()

    /**
     * 设置预览View
     */
    fun setPreview(view: View)

    fun setLifeCycleOwner(lifecycleOwner: LifecycleOwner)

    /**
     * 拍照
     */
    fun takePicture()

    /**
     * 开始录制视频
     */
    fun startRecordVideo()

    /**
     * 停止录制视频
     */
    fun stopRecordVideo()

    /**
     * 设置监听
     */
    fun setCameraListener(listener: CameraListener?)

    /**
     * 设置旋转
     */
    fun getDisplayOrientation(context: Context?, cameraId: Int): Int

    /**
     * 获取支持的预览大小
     */
    fun getSupportPreviewSize(): MutableList<Size>

    /**
     * 获取支持的图片大小
     */
    fun getSupportPictureSize(): MutableList<Size>

}