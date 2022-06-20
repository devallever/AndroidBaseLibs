package app.allever.android.lib.imageloader.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.logE
import app.allever.android.lib.core.function.imageloader.ILoader
import app.allever.android.lib.core.function.imageloader.ImageLoader
import app.allever.android.lib.core.helper.DisplayHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.BitmapImageViewTarget
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropCircleWithBorderTransformation
import java.io.File

object GlideLoader : ILoader {

    override fun init(context: Context) {

    }

    override fun load(resource: Any, imageView: ImageView, errorResId: Int?, placeholder: Int?) {
        if (!ImageLoader.checkCanLoad(imageView)) {
            return
        }
        val requestBuilder = Glide.with(imageView.context).load(resource)
        applyDefault(requestBuilder, errorResId, placeholder).into(imageView)
    }


    override fun loadCircle(
        resource: Any,
        imageView: ImageView,
        borderWidthDp: Int?,
        borderColor: Int?,
        errorResId: Int?,
        placeholder: Int?
    ) {
        if (!ImageLoader.checkCanLoad(imageView)) {
            return
        }

        val requestBuilder = Glide.with(imageView.context).load(resource)
//            .apply(RequestOptions.circleCropTransform())
            .apply(
                RequestOptions.bitmapTransform(
                    CropCircleWithBorderTransformation(
                        DisplayHelper.dip2px(
                            borderWidthDp ?: 0
                        ), borderColor ?: Color.parseColor("#00000000")
                    )
                )
            )
        applyDefault(requestBuilder, errorResId, placeholder).into(imageView)
    }

    override fun loadRound(
        resource: Any,
        imageView: ImageView,
        radiusDp: Float?,
        errorResId: Int?,
        placeholder: Int?
    ) {
        if (!ImageLoader.checkCanLoad(imageView)) {
            return
        }
        val requestBuilder = Glide.with(imageView.context)
            .asBitmap()
            .centerCrop()
            .load(resource)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
        applyDefault(requestBuilder, errorResId, placeholder)
        requestBuilder.into(object : BitmapImageViewTarget(imageView) {
            override fun setResource(resource: Bitmap?) {
                super.setResource(resource)
                val rbd =
                    RoundedBitmapDrawableFactory.create(imageView.context.resources, resource)
                rbd.cornerRadius = DisplayHelper.dip2px(radiusDp ?: 0F).toFloat()
                imageView.setImageDrawable(rbd)
            }
        })
    }


    override fun loadGif(resource: Any, imageView: ImageView) {
        if (!ImageLoader.checkCanLoad(imageView)) {
            return
        }
        Glide.with(imageView.context).asGif().load(resource).into(imageView)
    }

    override fun loadBlur(resource: Any, imageView: ImageView, radius: Float?) {
        if (!ImageLoader.checkCanLoad(imageView)) {
            return
        }

        Glide.with(imageView.context).load(resource)
            .sizeMultiplier(0.2F)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .apply(
                RequestOptions.bitmapTransform(
                    BlurTransformation(
                        DisplayHelper.dip2px(radius?.toInt() ?: 0)
                    )
                )
            )
            .into(imageView)
    }

    override fun download(url: String, block: (success: Boolean, file: File?) -> Unit) {
        Glide.with(App.context)
            .downloadOnly()
            .load(url)
            .listener(object : RequestListener<File> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<File>?,
                    isFirstResource: Boolean
                ): Boolean {
                    logE("下载失败: $url")
                    block(false, null)
                    return false
                }

                override fun onResourceReady(
                    resource: File?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<File>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    log("下载成功: $url")
                    block(true, resource)
                    return false
                }
            })
            .preload()
    }

    private fun applyDefault(
        requestBuilder: RequestBuilder<*>,
        errorResId: Int?,
        placeholder: Int?
    ): RequestBuilder<*> {
        errorResId?.let {
            requestBuilder.error(errorResId)
        }
        placeholder?.let {
            requestBuilder.placeholder(placeholder)
        }
        return requestBuilder
    }
}