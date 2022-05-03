package app.allever.android.lib.core.function.imageloader

import android.widget.ImageView

interface ILoader {
    fun load(resource: Any, imageView: ImageView)
    fun loadGif(resource: Any, imageView: ImageView)
    fun load(resource: Any, imageView: ImageView, errorResId: Int, placeholder: Int)
}