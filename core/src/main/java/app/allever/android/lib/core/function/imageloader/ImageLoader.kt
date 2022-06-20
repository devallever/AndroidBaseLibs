package app.allever.android.lib.core.function.imageloader

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.widget.ImageView
import androidx.fragment.app.Fragment
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.logE
import app.allever.android.lib.core.helper.CoroutineHelper
import app.allever.android.lib.core.helper.DisplayHelper
import app.allever.android.lib.core.util.FileIOUtils
import app.allever.android.lib.core.util.FileUtils
import app.allever.android.lib.core.util.MD5
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import java.io.File
import java.io.IOException

object ImageLoader {

    private lateinit var mLoaderEngine: ILoader
    private lateinit var mBuilder: Builder

    private val mDownloadRequestSet = mutableSetOf<String>()

    private val mOkHttpClient: OkHttpClient by lazy {
        OkHttpClient()
    }

    fun init(context: Context, loaderEngine: ILoader, builder: Builder) {
        mLoaderEngine = loaderEngine
        mBuilder = builder
        mLoaderEngine.init(context)
        FileUtils.createDir(mBuilder.cacheDir)
    }

    /**
     * @param resource resource
     * @param imageView imageView
     * @param loadOrigin 是否加载原图
     * @param errorResId errorResId
     * @param placeholder placeholder
     */
    fun load(
        resource: Any,
        imageView: ImageView,
        loadOrigin: Boolean = true,
        errorResId: Int? = mBuilder.errorResId,
        placeholder: Int? = mBuilder.placeholder
    ) {
        loadInternal(resource, loadOrigin) {
            mLoaderEngine.load(it ?: resource, imageView, errorResId, placeholder)
            downloadInternal(it, resource)
        }
    }

    fun loadCircle(
        resource: Any,
        imageView: ImageView,
        borderWidth: Int? = 0,
        borderColor: Int? = Color.parseColor("#00000000"),
        loadOrigin: Boolean = true,
        errorResId: Int? = mBuilder.errorResId,
        placeholder: Int? = mBuilder.placeholder
    ) {
        loadInternal(resource, loadOrigin) {
            mLoaderEngine.loadCircle(
                it ?: resource,
                imageView,
                borderWidth,
                borderColor,
                errorResId,
                placeholder
            )
            downloadInternal(it, resource)
        }
    }

    fun loadRound(
        resource: Any,
        imageView: ImageView,
        radius: Float? = DisplayHelper.dip2px(10).toFloat(),
        loadOrigin: Boolean = true,
        errorResId: Int? = mBuilder.errorResId,
        placeholder: Int? = mBuilder.placeholder
    ) {
        loadInternal(resource, loadOrigin) {
            mLoaderEngine.loadRound(it ?: resource, imageView, radius, errorResId, placeholder)
            downloadInternal(it, resource)
        }
    }

    fun loadGif(resource: Any, imageView: ImageView) {
        loadInternal(resource, true) {
            mLoaderEngine.loadGif(it ?: resource, imageView)
            downloadInternal(it, resource)
        }
    }

    fun loadBlur(
        resource: Any,
        imageView: ImageView,
        radius: Float?,
        loadOrigin: Boolean = true,
    ) {
        loadInternal(resource, loadOrigin) {
            mLoaderEngine.loadBlur(it ?: resource, imageView, radius)
            downloadInternal(it, resource)
        }
    }

    suspend fun download(
        url: String,
        block: ((success: Boolean, file: File?) -> Unit)? = null
    ) {
        val file = getCache(url)
        if (file != null) {
            block?.let { it(true, file) }
        } else {
            if (!mDownloadRequestSet.contains(url)) {
                mDownloadRequestSet.add(url)

                val requests = Request.Builder()
                    .url(url)
                    .build()

                mOkHttpClient.newCall(requests).enqueue(object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        mDownloadRequestSet.remove(url)
                        log("下载成功: $url")
                        val path = getCacheFilePath(url)
                        val result = FileIOUtils.writeFileFromBytesByStream(
                            path,
                            response.body?.byteStream()?.readBytes()
                        )
                        val file = if (result) {
                            log("保存成功: $path")
                            File(path)
                        } else {
                            logE("保存失败: $path")
                            null
                        }
                        CoroutineHelper.mainCoroutine.launch {
                            block?.let { it(result, file) }
                        }
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        mDownloadRequestSet.remove(url)
                        logE("下载失败: $url")
                        CoroutineHelper.mainCoroutine.launch {
                            block?.let { it(false, null) }
                        }
                    }
                })

//                mLoaderEngine.download(url) { success, originFile ->
//                    mDownloadRequestSet.remove(url)
//                    CoroutineHelper.mainCoroutine.launch {
//                        saveCache(success, url, originFile)
//                        block?.let { it(success, originFile) }
//                    }
//                }
            }
        }
    }

    private suspend fun saveCache(success: Boolean, url: String, src: File?) {
        withContext(Dispatchers.IO) {
            if (success) {
                val saveResult =
                    FileUtils.copy(src?.absolutePath ?: return@withContext, getCacheFilePath(url))
                log("保存成功：${saveResult} -> ${getCacheFilePath(url)}")
            }
        }
    }

    suspend fun getCache(url: String) = withContext(Dispatchers.IO) {
        val file = File(getCacheFilePath(url))
        if (file.exists()) {
            return@withContext file
        }
        return@withContext null
    }

    suspend fun clearCache() {
        withContext(Dispatchers.IO) {
            FileUtils.deleteAllInDir(mBuilder.cacheDir)
        }
    }

    fun errorResId() = mBuilder.errorResId
    fun placeholder() = mBuilder.placeholder

    private fun downloadInternal(file: File?, resource: Any) {
        if (resource is String && resource.startsWith("http")) {
            file ?: CoroutineHelper.mainCoroutine.launch {
                download(resource, null)
            }
        }
    }

    private fun loadInternal(resource: Any, loadOrigin: Boolean, block: (resource: File?) -> Unit) {
        //不是加载原图，那就直接返回
        if (!loadOrigin) {
            block(null)
            return
        }

        //加载原图，那就读缓存
        CoroutineHelper.mainCoroutine.launch {
            if (resource is String && resource.startsWith("http")) {
                block(getCache(resource))
            } else {
                block(null)
            }
        }
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

    class Builder {
        var errorResId: Int? = null
        var placeholder: Int? = null
        var cacheDir: String =
            (App.context.externalCacheDir?.absolutePath
                ?: App.context.cacheDir.absolutePath) + File.separator + "image"

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