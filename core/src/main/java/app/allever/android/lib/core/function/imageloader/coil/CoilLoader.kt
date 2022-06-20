package app.allever.android.lib.core.function.imageloader.coil

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.os.Build.VERSION.SDK_INT
import android.widget.ImageView
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.logE
import app.allever.android.lib.core.function.imageloader.ILoader
import app.allever.android.lib.core.helper.DisplayHelper
import app.allever.android.lib.core.util.FileIOUtils
import app.allever.android.lib.core.util.MD5
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.load
import coil.transform.RoundedCornersTransformation
import okhttp3.*
import java.io.File
import java.io.IOException

object CoilLoader : ILoader {

    private val mOkHttpClient: OkHttpClient by lazy {
        OkHttpClient()
    }

    override fun init(context: Context) {
        if (context is Application) {
            Coil.setImageLoader(context.initCoil())
        }
    }

    override fun load(resource: Any, imageView: ImageView, errorResId: Int?, placeholder: Int?) {
        imageView.load(resource) {
            errorResId?.let {
                error(it)
            }
            placeholder?.let {
                placeholder(it)
            }
            crossfade(true)
        }
    }

    override fun loadCircle(
        resource: Any,
        imageView: ImageView,
        borderWidthDp: Int?,
        borderColor: Int?,
        errorResId: Int?,
        placeholder: Int?
    ) {
        imageView.load(resource) {
            placeholder?.let {
                placeholder(it)
            }
            transformations(
                BorderCircleTransformation(
                    DisplayHelper.dip2px(borderWidthDp ?: 0),
                    borderColor ?: Color.parseColor("#00000000")
                )
            )
        }
    }

    override fun loadRound(
        resource: Any,
        imageView: ImageView,
        radiusDp: Float?,
        errorResId: Int?,
        placeholder: Int?
    ) {
        imageView.load(resource) {
            placeholder?.let {
                placeholder(it)
            }
            transformations(
                RoundedCornersTransformation(
                    radius = DisplayHelper.dip2px(radiusDp ?: 0f).toFloat()
                )
            )
        }
    }

    override fun loadGif(resource: Any, imageView: ImageView) {
        load(resource, imageView, null, null)
    }

    override fun loadBlur(resource: Any, imageView: ImageView, radius: Float?) {
        imageView.load(resource) {
            transformations(BlurTransformation(imageView.context, radius ?: 0f))
        }
    }

    override fun download(url: String, block: (success: Boolean, file: File?) -> Unit) {
        val requests = Request.Builder()
            .url(url)
            .build()

        mOkHttpClient.newCall(requests).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                log("下载成功: $url")
                val path =
                    App.context.externalCacheDir?.absolutePath + File.separator + MD5.getMD5Str(url)
                val result = FileIOUtils.writeFileFromBytesByStream(
                    path,
                    response.body?.byteStream()?.readBytes()
                )
                val file = if (result) {
                    log("保存成功: $url")
                    File(path)
                } else {
                    logE("保存失败: $url")
                    null
                }
                block(result, file)
            }

            override fun onFailure(call: Call, e: IOException) {
                logE("下载失败: $url")
                block(false, null)
            }
        })
    }

}

fun Application.initCoil(): ImageLoader {
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
