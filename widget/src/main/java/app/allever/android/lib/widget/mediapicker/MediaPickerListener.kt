package app.allever.android.lib.widget.mediapicker

import app.allever.android.lib.core.function.media.MediaBean

interface MediaPickerListener {
    /**
     *  点击确定（使用）回调
     */
    fun onPicked(
        all: MutableList<MediaBean>,
        imageList: MutableList<MediaBean>,
        videoList: MutableList<MediaBean>,
        audioList: MutableList<MediaBean>
    )
}