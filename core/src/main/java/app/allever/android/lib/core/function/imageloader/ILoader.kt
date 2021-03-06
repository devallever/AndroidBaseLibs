package app.allever.android.lib.core.function.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import java.io.File

interface ILoader {
    fun init(context: Context)

    fun load(resource: Any, imageView: ImageView, errorResId: Int?, placeholder: Int?)

    fun loadCircle(
        resource: Any,
        imageView: ImageView,
        borderWidthDp: Int?,
        borderColor: Int?,
        errorResId: Int?,
        placeholder: Int?
    )

    fun loadRound(
        resource: Any,
        imageView: ImageView,
        radiusDp: Float?,
        errorResId: Int?,
        placeholder: Int?
    )

    fun loadGif(resource: Any, imageView: ImageView)

    fun loadBlur(resource: Any, imageView: ImageView, radius: Float?)

    @Deprecated("")
    fun download(url: String, block: (success: Boolean, file: File?) -> Unit)
}