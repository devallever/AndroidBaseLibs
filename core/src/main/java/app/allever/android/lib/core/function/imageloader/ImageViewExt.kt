package app.allever.android.lib.core.function.imageloader

import android.graphics.Color
import android.widget.ImageView
import java.io.File

fun ImageView.load(
    resource: Any,
    loadOrigin: Boolean = true,
    errorResId: Int? = ImageLoader.errorResId(),
    placeholder: Int? = ImageLoader.placeholder()
) {
    ImageLoader.load(resource, this, loadOrigin, errorResId, placeholder)
}

fun ImageView.loadGif(resource: Any) {
    ImageLoader.loadGif(resource, this)
}

fun ImageView.loadCircle(
    any: Any,
    borderWidth: Int = 0,
    borderColor: Int? = Color.parseColor("#00000000"),
    loadOrigin: Boolean = true,
    errorResId: Int? = ImageLoader.errorResId(),
    placeholder: Int? = ImageLoader.placeholder()
) {
    ImageLoader.loadCircle(any, this, borderWidth, borderColor, loadOrigin, errorResId, placeholder)
}

fun ImageView.loadRound(
    any: Any,
    radius: Float = 8f,
    loadOrigin: Boolean = true,
    errorResId: Int? = ImageLoader.errorResId(),
    placeholder: Int? = ImageLoader.placeholder()
) {
    ImageLoader.loadRound(any, this, radius, loadOrigin, errorResId, placeholder)
}

fun ImageView.loadBlur(any: Any, radius: Float = 10f, loadOrigin: Boolean = true) {
    ImageLoader.loadBlur(any, this, radius, loadOrigin)
}

suspend fun downloadImg(any: String, block: (success: Boolean, file: File?) -> Unit) {
    ImageLoader.download(any, block)
}
