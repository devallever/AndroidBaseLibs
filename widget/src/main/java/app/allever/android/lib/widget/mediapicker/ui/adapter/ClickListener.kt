package app.allever.android.lib.widget.mediapicker.ui.adapter

import app.allever.android.lib.widget.mediapicker.MediaItem

interface ClickListener {
    fun onItemClick(mediaItem: MediaItem, position: Int)

    fun onItemLongClick(mediaItem: MediaItem, position: Int) {}
}