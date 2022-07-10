package app.allever.android.lib.widget.mediapicker.ui

interface IMediaPicker {
    /**
     * 当选中目录后调用刷新每个界面数据
     */
    fun update(path: String)
}