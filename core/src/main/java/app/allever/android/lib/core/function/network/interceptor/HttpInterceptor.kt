package app.allever.android.lib.core.function.network.interceptor

import app.allever.android.lib.core.BuildConfig
import app.allever.android.lib.core.ext.log
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset

class HttpInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
        requestBuilder.method(originalRequest.method, originalRequest.body)
        val request = requestBuilder.build()
        val response = chain.proceed(request)
        val responseBody = response.body
        val responseBodyString = responseBody?.string() ?: ""
        if (BuildConfig.DEBUG) {
            log("ILogger HttpInterceptor", "请求链接 = " + request.url)
            log("ILogger HttpInterceptor", "请求体 = " + getRequestInfo(request))
            log("ILogger HttpInterceptor", "请求结果 = $responseBodyString")
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