package app.allever.android.lib.core.function.imageloader

import android.app.Activity
import android.widget.ImageView
import androidx.fragment.app.Fragment

object ImageLoader: ILoader {

    private lateinit var mLoaderEngine: ILoader

    fun init(loaderEngine: ILoader) {
        mLoaderEngine = loaderEngine
    }

    override fun load(resource: Any, imageView: ImageView) {
        mLoaderEngine.load(resource, imageView)
    }

    override fun load(resource: Any, imageView: ImageView, errorResId: Int, placeholder: Int) {
        mLoaderEngine.load(resource, imageView, errorResId, placeholder)
    }

    override fun loadGif(resource: Any, imageView: ImageView) {
        mLoaderEngine.loadGif(resource, imageView)
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
        }

        return true
    }
}