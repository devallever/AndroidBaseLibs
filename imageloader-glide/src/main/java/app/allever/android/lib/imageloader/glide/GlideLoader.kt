package app.allever.android.lib.imageloader.glide

import android.widget.ImageView
import app.allever.android.lib.core.function.imageloader.ILoader
import app.allever.android.lib.core.function.imageloader.ImageLoader
import com.bumptech.glide.Glide

object GlideLoader : ILoader {

    override fun load(resource: Any, imageView: ImageView) {
        if (!ImageLoader.checkCanLoad(imageView)) {
            return
        }
        Glide.with(imageView.context).asBitmap().load(resource).into(imageView)
    }

    override fun loadGif(resource: Any, imageView: ImageView) {
        if (!ImageLoader.checkCanLoad(imageView)) {
            return
        }
        Glide.with(imageView.context).asGif().load(resource).into(imageView)
    }

    override fun load(resource: Any, imageView: ImageView, errorResId: Int, placeholder: Int) {
        if (!ImageLoader.checkCanLoad(imageView)) {
            return
        }
        Glide.with(imageView.context).asBitmap().load(resource).error(errorResId).placeholder(placeholder)
            .into(imageView)
    }
}