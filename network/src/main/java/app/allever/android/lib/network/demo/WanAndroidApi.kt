package app.allever.android.lib.network.demo

import retrofit2.http.GET

interface WanAndroidApi {
    @GET("banner/json")
    suspend fun getBanner(): BaseResponse<List<BannerData>>

}