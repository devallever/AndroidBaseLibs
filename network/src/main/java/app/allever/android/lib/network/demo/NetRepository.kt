package app.allever.android.lib.network.demo

import app.allever.android.lib.core.function.network.NetworkHandler
import app.allever.android.lib.core.function.network.ResponseCallback
import app.allever.android.lib.core.function.network.cache.ResponseCache
import app.allever.android.lib.network.ApiService
import app.allever.android.lib.network.RetrofitCallback
import kotlinx.coroutines.delay

object NetRepository : NetworkHandler() {
    private val wanAndroidApi by lazy {
        ApiService.get(WanAndroidApi::class.java)
    }

    suspend fun getBannerForJava() = requestForJava {
        wanAndroidApi.getBannerForJava()
    }

    suspend fun getBanner() = request {
        wanAndroidApi.getBanner()
    }

    suspend fun getBanner(responseCache: ResponseCache<*>? = null) =
        request(BaseResponse::class.java, responseCache) {
            wanAndroidApi.getBanner()
        }

    suspend fun getBannerForLiveData(responseCache: ResponseCache<*>? = null) =
        requestLiveData(BaseResponse::class.java, responseCache) {
            wanAndroidApi.getBannerForJava()
        }

    suspend fun test(): String {
        delay(1000)
        return "hello"

    }

    @Deprecated("不使用缓存")
    fun getBannerCall(callback: ResponseCallback<List<BannerData>>) {
        wanAndroidApi.getBannerCall().enqueue(RetrofitCallback(callback))
    }

    fun getBannerCall(
        responseCache: ResponseCache<*>? = null,
        callback: ResponseCallback<List<BannerData>>
    ) {
        enqueue(responseCache, callback) {
            wanAndroidApi.getBannerCall().enqueue(RetrofitCallback(responseCache, callback))
        }
    }

    suspend fun getData(): BaseResponse<List<BannerData>> {
        val response = BaseResponse<List<BannerData>>()
        return response
    }

}