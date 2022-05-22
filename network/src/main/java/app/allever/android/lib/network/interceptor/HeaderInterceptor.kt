package app.allever.android.lib.network.interceptor

import app.allever.android.lib.network.HttpConfig
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody

class HeaderInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()
        requestBuilder.addHeader("Accept-Encoding", "gzip")
        requestBuilder.addHeader("Accept", "application/json")
        requestBuilder.addHeader("Content-Type", "application/json; charset=utf-8")

        HttpConfig.headers.map {
            requestBuilder.addHeader(it.key, it.value)
        }

        requestBuilder.method(originalRequest.method, originalRequest.body)

        val request = requestBuilder.build()
        val response = chain.proceed(request)
        val responseBody = response.body

        val responseBodyString = responseBody?.string() ?: "null"
        val body = responseBodyString.toByteArray()
            .toResponseBody(if (responseBody == null) "application/json".toMediaTypeOrNull() else responseBody.contentType())
        return response.newBuilder().body(body).build()
    }
}