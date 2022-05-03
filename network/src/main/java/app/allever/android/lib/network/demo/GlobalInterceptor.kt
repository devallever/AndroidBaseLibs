package app.allever.android.lib.network.demo

import app.allever.android.lib.core.ext.loge
import app.allever.android.lib.network.BuildConfig
import okhttp3.*
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset

class GlobalInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
            .addHeader("Accept-Encoding", "gzip")
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .method(originalRequest.method(), originalRequest.body())
        val request = requestBuilder.build()
        val response = chain.proceed(request)
        val responseBody = response.body()
        val responseBodyString = if (responseBody == null) "null" else responseBody.string()
        if (BuildConfig.DEBUG) {
            loge("LogInterceptor", "请求链接= " + request.url())
            request.headers().toMultimap().map {
                loge("LogInterceptor", "请求头= ${it.key}: ${it.value}" )
            }
            loge("LogInterceptor", "请求体= " + getRequestInfo(request))
            loge("LogInterceptor", "请求结果= $responseBodyString")
        }
        val body = ResponseBody.create(
            if (responseBody == null) MediaType.parse("application/json") else responseBody.contentType(),
            responseBodyString.toByteArray()
        )
        return response.newBuilder().body(body).build()
    }

    /**
     * 打印请求消息
     *
     * @param request 请求的对象
     */
    private fun getRequestInfo(request: Request?): String? {
        var str = ""
        if (request == null) {
            return str
        }
        val requestBody = request.body() ?: return str
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