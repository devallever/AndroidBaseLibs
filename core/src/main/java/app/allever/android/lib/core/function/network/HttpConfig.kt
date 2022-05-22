package app.allever.android.lib.core.function.network

import okhttp3.Interceptor

object HttpConfig {
    var successCode = 0

    var baseUrl: String = ""

    val interceptors = mutableSetOf<Interceptor>()
    val networkInterceptors = mutableListOf<Interceptor>()

    val headers = mutableMapOf<String, String>()


    fun build(): HttpConfig {
        return this
    }

    fun successCode(code: Int): HttpConfig {
        successCode = code
        return this
    }

    fun baseUrl(baseUrl: String): HttpConfig {
        HttpConfig.baseUrl = baseUrl
        return this
    }

    fun interceptor(interceptor: Interceptor): HttpConfig {
        interceptors.add(interceptor)
        return this
    }

    fun networkInterceptor(interceptor: Interceptor): HttpConfig {
        networkInterceptors.add(interceptor)
        return this
    }

    fun header(key: String, value: String): HttpConfig {
        headers[key] = value
        return this
    }

    fun init(httpConfig: IHttpConfig) {
        httpConfig.init()
    }
}