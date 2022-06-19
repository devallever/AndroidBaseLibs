package app.allever.android.lib.core.function.imageloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView

fun ImageView.load(resource: Any, errorIcon: Int = 0, placeHolder: Int = 0) {
    ImageLoader.load(resource, this, errorIcon, placeHolder)
}

fun ImageView.loadGif(resource: Any) {
    ImageLoader.loadGif(resource, this)
}

fun ImageView.loadCircle(
    any: Any,
    borderWidth: Int = 0,
    borderColor: Int? = Color.parseColor("#00000000"),
    errorResId: Int? = null,
    placeholder: Int? = null
) {
    ImageLoader.loadCircle(any, this, borderWidth, borderColor, errorResId, placeholder)
}

fun ImageView.loadRound(any: Any, radius: Float = 8f, errorResId: Int? = null, placeholder: Int? = null) {
    ImageLoader.loadRound(any, this, radius, errorResId, placeholder)
}

fun ImageView.loadBlur(any: Any, radius: Float = 10f) {
    ImageLoader.loadBlur(any, this, radius)
}

fun Context.downloadImg(any: String, block: (bitmap: Bitmap) -> Unit) {
    ImageLoader.download(any, block)
}
