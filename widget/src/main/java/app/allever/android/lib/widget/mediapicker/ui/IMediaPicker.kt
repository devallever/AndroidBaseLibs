package app.allever.android.lib.widget.mediapicker.ui

import app.allever.android.lib.core.function.media.FolderBean

interface IMediaPicker {
    fun update(list: MutableList<FolderBean>)
}