package app.allever.android.lib.network

import okhttp3.Interceptor

object HttpConfig {

    lateinit var baseUrl: String

    val interceptors = mutableSetOf<Interceptor>()

    val headers = mutableMapOf<String, String>()

    fun build(): HttpConfig {
        return this
    }

    fun baseUrl(baseUrl: String): HttpConfig {
        this.baseUrl = baseUrl
        return this
    }

    fun interceptor(interceptor: Interceptor): HttpConfig {
        interceptors.add(interceptor)
        return this
    }

    fun header(key: String, value: String): HttpConfig {
        headers[key] = value
        return this
    }

}