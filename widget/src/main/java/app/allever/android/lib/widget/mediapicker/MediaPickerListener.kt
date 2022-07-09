package app.allever.android.lib.widget.mediapicker

import app.allever.android.lib.core.function.media.MediaBean

interface MediaPickerListener {
    fun onPicked(
        all: MutableList<MediaBean>,
        imageList: MutableList<MediaBean>?,
        videoList: MutableList<MediaBean>?,
        audioList: MutableList<MediaBean>?
    )
}