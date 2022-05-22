package app.allever.android.lib.core.function.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class GsonInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request())
    }
}