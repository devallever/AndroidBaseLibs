package app.allever.android.lib.imagecompress.luban

import android.content.Context
import android.text.TextUtils
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.logE
import app.allever.android.lib.core.function.imagecompress.CompressResult
import app.allever.android.lib.core.function.imagecompress.IIMageCompress
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class LubanCompress : IIMageCompress {
    override suspend fun compress(context: Context, path: String): CompressResult {
        return compressInternal(context, mutableListOf(path))
    }

    override suspend fun compress(context: Context, pathList: MutableList<String>): CompressResult {
        return compressInternal(context, pathList)
    }

    private suspend fun compressInternal(
        context: Context,
        pathList: MutableList<String>
    ): CompressResult {
        return suspendCoroutine {
            val result = CompressResult()
            Luban.with(context)
                .load<Any>(pathList as List<Any>?)
                .ignoreBy(0)
                .setTargetDir(context.cacheDir.absolutePath)
                .filter { path ->
                    !(TextUtils.isEmpty(path) /*|| path.lowercase(Locale.getDefault()).endsWith(".gif")*/)
                }
                .setCompressListener(object : OnCompressListener {
                    override fun onStart() {
                        //压缩开始前调用，可以在方法内启动 loading UI
                    }

                    override fun onSuccess(file: File) {
                        log("鲁班图片压缩成功：${file.path}")
                        //压缩成功后调用，返回压缩后的图片文件
                        result.list.add(file.path)
                        if (result.list.size == pathList.size) {
                            result.success = false
                            it.resume(result)
                        }
                    }

                    override fun onError(e: Throwable) {
                        e.printStackTrace()
                        logE("鲁班图片压缩失败：${e.message}")
                        //当压缩过程出现问题时调用
                        result.success = false
                        it.resume(result)
                    }
                }).launch()
        }
    }
}