package app.allever.android.lib.network.demo

import app.allever.android.lib.core.ext.logE
import app.allever.android.lib.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset

class GlobalInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Accept-Encoding", "gzip")
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .method(originalRequest.method, originalRequest.body)
        val request = requestBuilder.build()
        val response = chain.proceed(request)
        val responseBody = response.body
        val responseBodyString = responseBody?.string() ?: "null"
        if (BuildConfig.DEBUG) {
            logE("LogInterceptor", "请求链接= " + request.url)
            request.headers.toMultimap().map {
                logE("LogInterceptor", "请求头= ${it.key}: ${it.value}")
            }
            logE("LogInterceptor", "请求体= " + getRequestInfo(request))
            logE("LogInterceptor", "请求结果= $responseBodyString")
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
}