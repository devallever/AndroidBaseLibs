package app.allever.android.lib.widget.mediapicker

import app.allever.android.lib.core.function.media.MediaBean

class MediaItem(val data: MediaBean) {
    var isChecked = false

    //歌曲特有----------
    var playing = false
    var loaded = false
    //歌曲特有----------
}