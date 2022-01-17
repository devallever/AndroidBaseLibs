package app.allever.android.lib.network.interceptor

import app.allever.android.lib.core.loge
import okhttp3.logging.HttpLoggingInterceptor

class LoggerInterceptor: HttpLoggingInterceptor.Logger {
    override fun log(message: String) {
        loge("LoggerInterceptor", message)
    }
}