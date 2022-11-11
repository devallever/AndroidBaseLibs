package app.allever.android.lib.core.function.camera

import android.graphics.Bitmap

interface CameraListener {
    /**
     * 预览相机
     */
    fun onPreview(data: ByteArray, imageFormat: Int) {}

    fun onTakePicture(data: ByteArray?, bitmap: Bitmap?, imageFormat: Int) {}

    fun onError(msg: String) {}
}