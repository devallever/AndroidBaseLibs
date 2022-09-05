package app.allever.android.lib.widget.mediapicker

import app.allever.android.lib.core.function.media.MediaBean

class PickerResult {
    val all = mutableListOf<MediaBean>()
    val imageList = mutableListOf<MediaBean>()
    val videoList = mutableListOf<MediaBean>()
    val audioList = mutableListOf<MediaBean>()
}