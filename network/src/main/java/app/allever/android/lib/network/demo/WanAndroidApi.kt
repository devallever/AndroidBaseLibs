package app.allever.android.lib.network.demo

import app.allever.android.lib.network.response.BaseResponse
import retrofit2.http.GET

interface WanAndroidApi {
    @GET("banner/json")
    suspend fun getBanner(): BaseResponse<List<BannerData>>

}