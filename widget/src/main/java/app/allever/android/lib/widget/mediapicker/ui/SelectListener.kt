package app.allever.android.lib.widget.mediapicker.ui

import app.allever.android.lib.widget.mediapicker.MediaItem

interface SelectListener {
    fun onItemSelected(mediaItem: MediaItem)
    fun onConfirm()
}