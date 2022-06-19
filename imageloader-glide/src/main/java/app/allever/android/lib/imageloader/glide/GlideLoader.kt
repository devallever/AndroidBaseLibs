package app.allever.android.lib.imageloader.glide

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
        Glide.with(imageView.context)
            .asBitmap()
            .error(errorResId)
            .placeholder(placeholder ?: 0)
            .load(resource).into(imageView)
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

        Glide.with(imageView.context).load(resource)
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
            .error(errorResId)
            .placeholder(placeholder ?: 0)
            .into(imageView)
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
        Glide.with(imageView.context)
            .asBitmap()
            .centerCrop()
            .load(resource)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(object : BitmapImageViewTarget(imageView) {
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

    override fun download(url: String, block: (success: Boolean, bitmap: Bitmap?) -> Unit) {
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
                    val bitmap = BitmapFactory.decodeFile(resource?.absolutePath ?: "")
                    block(true, bitmap)
                    return false
                }
            })
            .preload()
    }
}