package app.allever.android.lib.core.function.imageloader

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
import androidx.fragment.app.Fragment
import app.allever.android.lib.core.helper.DisplayHelper

object ImageLoader {

    private lateinit var mLoaderEngine: ILoader
    private lateinit var mBuilder: Builder

    fun init(context: Context, loaderEngine: ILoader, builder: Builder) {
        mLoaderEngine = loaderEngine
        mBuilder = builder
        mLoaderEngine.init(context)
    }

    fun load(
        resource: Any,
        imageView: ImageView,
        errorResId: Int? = mBuilder.errorResId,
        placeholder: Int? = mBuilder.placeholder
    ) {
        mLoaderEngine.load(resource, imageView, errorResId, placeholder)
    }

    fun loadCircle(
        resource: Any,
        imageView: ImageView,
        borderWidth: Int? = 0,
        borderColor: Int? = Color.parseColor("#00000000"),
        errorResId: Int? = mBuilder.errorResId,
        placeholder: Int? = mBuilder.placeholder
    ) {
        mLoaderEngine.loadCircle(
            resource,
            imageView,
            borderWidth,
            borderColor,
            errorResId,
            placeholder
        )
    }

    fun loadRound(
        resource: Any,
        imageView: ImageView,
        radius: Float? = DisplayHelper.dip2px(10).toFloat(),
        errorResId: Int? = mBuilder.errorResId,
        placeholder: Int? = mBuilder.placeholder
    ) {
        mLoaderEngine.loadRound(resource, imageView, radius, errorResId, placeholder)
    }


    fun loadGif(resource: Any, imageView: ImageView) {
        mLoaderEngine.loadGif(resource, imageView)
    }

    fun loadBlur(resource: Any, imageView: ImageView, radius: Float?) {
        mLoaderEngine.loadBlur(resource, imageView, radius)
    }

    fun download(url: String, block: (bitmap: Bitmap) -> Unit) {
        mLoaderEngine.download(url, block)
    }

    fun checkCanLoad(imageView: ImageView): Boolean {
        val context = imageView.context
        if (context is Activity) {
            if (context.isDestroyed || context.isFinishing) {
                return false
            }
        }

        if (context is Fragment) {
            if (context.isDetached || context.fragmentManager == null) {
                return false
            }

            if (context.context == null) {
                return false
            }
        }

        return true
    }

    class Builder {
        var errorResId: Int? = null
        var placeholder: Int? = null

        companion object {
            fun create(): Builder {
                return Builder()
            }
        }

        fun error(errorResId: Int): Builder {
            this.errorResId = errorResId
            return this
        }

        fun default(placeholder: Int): Builder {
            this.placeholder = placeholder
            return this
        }
    }
}