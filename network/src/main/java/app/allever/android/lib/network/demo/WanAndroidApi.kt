package app.allever.android.lib.network.demo

import retrofit2.Call
import retrofit2.http.GET

interface WanAndroidApi {
    @GET("banner/json")
    suspend fun getBanner(): BaseResponse<List<BannerData>>

    @GET("banner/json")
    suspend fun getBannerForJava(): BaseResponse<*>

    @GET("banner/json")
    fun getBannerCall(): Call<BaseResponse<List<BannerData>>>

}