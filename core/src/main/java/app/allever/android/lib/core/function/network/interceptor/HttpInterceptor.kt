package app.allever.android.lib.core.function.network.interceptor

import android.util.Log
import app.allever.android.lib.core.BuildConfig
import app.allever.android.lib.core.ext.logE
import app.allever.android.lib.core.function.network.HttpConfig
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset

class HttpInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
        HttpConfig.headers.map {
            requestBuilder.addHeader(it.key, it.value)
        }
        requestBuilder
            .addHeader("Accept-Encoding", "gzip")
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .method(originalRequest.method, originalRequest.body)
        val request = requestBuilder.build()
        val response = chain.proceed(request)
        val responseBody = response.body
        val responseBodyString = responseBody?.string() ?: ""
        if (BuildConfig.DEBUG) {
            val tag = "HttpInterceptor"
            logE(tag, "\n\nHttp ==> Start")
            logE(tag, "请求链接 = " + request.url)
            request.headers.toMultimap().map {
                logE(tag, "请求头\t = ${it.key}: ${it.value}")
            }
            logE(tag, "请求体\t = " + getRequestInfo(request))
            iLargeChar(tag, "请求结果 = $responseBodyString")
            logE(tag,"Http ==> End")
        }
        val body = responseBodyString.toByteArray()
            .toResponseBody(if (responseBody == null) "application/json".toMediaTypeOrNull() else responseBody.contentType())
        return response.newBuilder().body(body).build()
    }

    /**
     * 打印请求消息
     *
     * @param request 请求的对象
     */
    private fun getRequestInfo(request: Request?): String {
        var str = ""
        if (request == null) {
            return str
        }
        val requestBody = request.body ?: return str
        try {
            val bufferedSink = Buffer()
            requestBody.writeTo(bufferedSink)
            val charset = Charset.forName("utf-8")
            str = bufferedSink.readString(charset)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return str
    }

    private fun iLargeChar(tag: String?, msg: String?) {
        var msg = msg
        if (tag == null || tag.isEmpty() || msg == null || msg.isEmpty()) return
        val segmentSize = 3 * 1024
        val length = msg.length.toLong()
        if (length > segmentSize) {
            while (msg!!.length > segmentSize) {
                val logContent = msg.substring(0, segmentSize)
                msg = msg.replace(logContent, "")
                Log.e(tag, logContent)
            }
        }
        Log.e(tag, msg)
    }
}