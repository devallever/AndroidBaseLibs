package app.allever.android.lib.core.function.imageloader.coil

import android.app.Application
import android.os.Build.VERSION.SDK_INT
import android.widget.ImageView
import app.allever.android.lib.core.helper.DisplayHelper
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation

fun Application.initCoil(placeholder: Int? = null) {
    defaultPlaceHolder = placeholder
    ImageLoader.Builder(this)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }
        }
        .build()
}

private val circleCropTransformation: CircleCropTransformation by lazy {
    CircleCropTransformation()
}

private var defaultPlaceHolder: Int? = null

fun ImageView.load(any: Any, placeholder: Int? = defaultPlaceHolder) {
    load(any) {
        placeholder?.let {
            placeholder(it)
        }
        crossfade(true)
    }
}

fun ImageView.loadCircle(any: Any, placeholder: Int? = defaultPlaceHolder) {

    load(any) {
        placeholder?.let {
            placeholder(it)
        }
        crossfade(true)
        transformations(circleCropTransformation)
    }
}

fun ImageView.loadRound(any: Any, radius: Float = 8f, placeholder: Int? = defaultPlaceHolder) {
    load(any) {
        placeholder?.let {
            placeholder(it)
        }
        crossfade(true)
        transformations(
            RoundedCornersTransformation(
                radius = DisplayHelper.dip2px(radius).toFloat()
            )
        )
    }
}

fun ImageView.loadBlur(any: Any, radius: Float = 10f, placeholder: Int? = defaultPlaceHolder) {
    load(any) {
        placeholder?.let {
            placeholder(it)
        }
        crossfade(true)
        transformations(BlurTransformation(context, radius))
    }
}

