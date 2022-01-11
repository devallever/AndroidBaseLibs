package app.allever.android.lib.network

import okhttp3.Interceptor

object HttpConfig {

    lateinit var baseUrl: String

    val interceptors = mutableSetOf<Interceptor>()

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

}