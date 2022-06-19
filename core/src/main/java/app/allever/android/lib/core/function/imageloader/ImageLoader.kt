package app.allever.android.lib.core.function.imageloader

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.widget.ImageView
import androidx.fragment.app.Fragment
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.helper.CoroutineHelper
import app.allever.android.lib.core.helper.DisplayHelper
import app.allever.android.lib.core.util.BitmapUtils
import app.allever.android.lib.core.util.MD5
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

object ImageLoader {

    private lateinit var mLoaderEngine: ILoader
    private lateinit var mBuilder: Builder

    private val mDownloadRequestSet = mutableSetOf<String>()

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
        downloadInternal(resource)
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
        downloadInternal(resource)
    }

    fun loadRound(
        resource: Any,
        imageView: ImageView,
        radius: Float? = DisplayHelper.dip2px(10).toFloat(),
        errorResId: Int? = mBuilder.errorResId,
        placeholder: Int? = mBuilder.placeholder
    ) {
        mLoaderEngine.loadRound(resource, imageView, radius, errorResId, placeholder)
        downloadInternal(resource)
    }


    fun loadGif(resource: Any, imageView: ImageView) {
        mLoaderEngine.loadGif(resource, imageView)
        downloadInternal(resource)
    }

    fun loadBlur(resource: Any, imageView: ImageView, radius: Float?) {
        mLoaderEngine.loadBlur(resource, imageView, radius)
        downloadInternal(resource)
    }

    fun download(url: String, block: ((success: Boolean, bitmap: Bitmap?) -> Unit)? = null) {
        CoroutineHelper.threadCoroutine.launch {
            val file = getCache(url)
            if (file != null) {
                launch(Dispatchers.Main) {
                    block?.let { it(true, BitmapFactory.decodeFile(file.absolutePath)) }
                }
            } else {
                if (!mDownloadRequestSet.contains(url)) {
                    mDownloadRequestSet.add(url)
                    mLoaderEngine.download(url) { success, bitmap ->
                        mDownloadRequestSet.remove(url)
                        if (success) {
                            val saveResult = BitmapUtils.saveBitmap2File(bitmap, getCacheFilePath(url))
                            log("保存成功：${saveResult}")
                        }
                        App.mainHandler.post {
                            block?.let { it(success, bitmap) }
                        }
                        //为啥子不执行?
//                        CoroutineHelper.mainCoroutine.launch {
//                            log("下载成功: bitmap = ${bitmap != null}")
//                            block?.let { it(success, bitmap) }
//                        }
//                        launch(Dispatchers.Main) {
//                            log("下载成功: bitmap = ${bitmap != null}")
//                            block?.let { it(success, bitmap) }
//                        }
                    }
                }
            }
        }
    }

    private fun downloadInternal(resource: Any) {
        if (resource is String && resource.startsWith("http")) {
            download(resource, null)
        }
    }

    fun getCache(url: String): File? {
        val file = File(getCacheFilePath(url))
        if (file.exists()) {
            return file
        }
        return null
    }

    private fun getCacheFilePath(url: String): String {
        return "${mBuilder.cacheDir}${File.separator}${MD5.getMD5Str(url)}"
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

    fun errorResId() = mBuilder.errorResId
    fun placeholder() = mBuilder.placeholder

    class Builder {
        var errorResId: Int? = null
        var placeholder: Int? = null
        var cacheDir: String =
            App.context.externalCacheDir?.absolutePath ?: App.context.cacheDir.absolutePath

        companion object {
            fun create(): Builder {
                return Builder()
            }
        }

        fun error(errorResId: Int): Builder {
            this.errorResId = errorResId
            return this
        }

        fun placeholder(placeholder: Int): Builder {
            this.placeholder = placeholder
            return this
        }
    }
}