package app.allever.android.lib.core.function.imageloader.coil

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build.VERSION.SDK_INT
import android.widget.ImageView
import app.allever.android.lib.core.helper.DisplayHelper
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import coil.transform.RoundedCornersTransformation

fun Application.initCoil(placeholder: Int? = null): ImageLoader {
    defaultPlaceHolder = placeholder
    return ImageLoader.Builder(this)
        .components {
            if (SDK_INT >= 28) {
                add(ImageDecoderDecoder.Factory())
            } else {
                add(GifDecoder.Factory())
            }

            add(SvgDecoder.Factory())
//            add(VideoFrameDecoder.Factory())
        }
        .crossfade(true)
        .build()
}

private var defaultPlaceHolder: Int? = null

fun ImageView.load(any: Any, placeholder: Int? = defaultPlaceHolder) {
    load(any) {
        placeholder?.let {
            placeholder(it)
        }
    }
}

fun ImageView.loadCircle(
    any: Any,
    borderWidth: Int = 0,
    borderColor: Int = Color.parseColor("#00000000"),
    placeholder: Int? = defaultPlaceHolder
) {

    load(any) {
        placeholder?.let {
            placeholder(it)
        }
        transformations(BorderCircleTransformation(DisplayHelper.dip2px(borderWidth), borderColor))
    }
}

fun ImageView.loadRound(any: Any, radius: Float = 8f, placeholder: Int? = defaultPlaceHolder) {
    load(any) {
        placeholder?.let {
            placeholder(it)
        }
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
        transformations(BlurTransformation(context, radius))
    }
}

fun Context.downloadImg(any: Any, block: (bitmap: Bitmap) -> Unit) {
    val request = ImageRequest.Builder(this)
        .data(any)
        .target(
            onStart = { placeholder ->
                // Handle the placeholder drawable.
            },
            onSuccess = { result ->
                // Handle the successful result.
                val bitmapDrawable = result as BitmapDrawable
                val bitmap = bitmapDrawable.bitmap
                block(bitmap)
            },
            onError = { error ->
                // Handle the error drawable.
            }
        )
        .build()
    imageLoader.enqueue(request)
}

