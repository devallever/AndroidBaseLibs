package app.allever.android.lib.widget.mediapicker.ui

import app.allever.android.lib.widget.mediapicker.MediaItem

interface SelectListener {
    /**
     * 选中item后回调刷新上级页面选中数据
     */
    fun onItemSelected(mediaItem: MediaItem)
}