package app.allever.android.lib.network

import app.allever.android.lib.core.app.App
import app.allever.android.lib.network.interceptor.HeaderInterceptor
import app.allever.android.lib.network.interceptor.HttpInterceptor
import app.allever.android.lib.network.interceptor.LoggerInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File

object ApiService {
    private val mRetrofit by lazy {

        val builder = OkHttpClient.Builder()

        //日志拦截
        val loggingInterceptor = HttpLoggingInterceptor(LoggerInterceptor())
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)
        builder.addInterceptor(HttpInterceptor())

        //添加外部拦截器
        HttpConfig.interceptors.map {
            builder.addInterceptor(it)
        }

        HttpConfig.networkInterceptors.map {
            builder.addNetworkInterceptor(it)
        }

        //缓存容量
        val SIZE_OF_CACHE = (100 * 1024 * 1024).toLong() // 100 MB

        //缓存路径
        val cacheFile: String = App.context.externalCacheDir.toString() + "/http"
        val cache = Cache(File(cacheFile), SIZE_OF_CACHE)
        builder.cache(cache)

        //请求头拦截
        builder.addInterceptor(HeaderInterceptor())
        Retrofit.Builder()
            .client(builder.build())
            .baseUrl(HttpConfig.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }

    fun <T> get(apiServiceClass: Class<T>): T = mRetrofit.create(apiServiceClass)
}