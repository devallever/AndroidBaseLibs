package app.allever.android.lib.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiService {
    private val mRetrofit by lazy {

        val builder = OkHttpClient.Builder()
        //日志拦截
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)

        //添加外部拦截器
        HttpConfig.interceptors.map {
            builder.addInterceptor(it)
        }

        //请求头拦截
//        builder.addInterceptor(HttpHeaderInterceptor())
        Retrofit.Builder()
            .client(builder.build())
            .baseUrl(HttpConfig.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> get(apiServiceClass: Class<T>): T = mRetrofit.create(apiServiceClass)
}