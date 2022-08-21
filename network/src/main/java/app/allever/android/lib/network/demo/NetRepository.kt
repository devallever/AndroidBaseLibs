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

    @Deprecated("")
    suspend fun getBannerForJava() = requestForJava {
        wanAndroidApi.getBannerForJava()
    }

    @Deprecated("")
    suspend fun getBanner() = request {
        wanAndroidApi.getBanner()
    }

    /**
     * Java回调方式
     */
    fun getBannerCall(
        responseCache: ResponseCache<*>? = null,
        callback: ResponseCallback<List<BannerData>>
    ) {
        enqueue(responseCache, callback) {
            wanAndroidApi.getBannerCall().enqueue(RetrofitCallback(responseCache, callback))
        }
    }

    /**
     * kotlin协程
     */
    suspend fun getBanner(responseCache: ResponseCache<*>? = null) =
        request(BaseResponse::class.java, responseCache) {
            wanAndroidApi.getBanner()
        }

    /**
     * kotlin协程 + LiveData 方式一
     */
    suspend fun getBannerForLiveData(responseCache: ResponseCache<*>? = null) =
        requestLiveData(BaseResponse::class.java, responseCache) {
            wanAndroidApi.getBanner()
        }

    /**
     * kotlin协程 + LiveData 方式一
     */
    fun getBannerWithLiveData(responseCache: ResponseCache<*>? = null) =
        requestLiveData2(BaseResponse::class.java, responseCache) {
            wanAndroidApi.getBanner()
        }

    suspend fun test(): String {
        delay(1000)
        return "hello"

    }

    @Deprecated("不使用缓存")
    fun getBannerCall(callback: ResponseCallback<List<BannerData>>) {
        wanAndroidApi.getBannerCall().enqueue(RetrofitCallback(callback))
    }

    suspend fun getData(): BaseResponse<List<BannerData>> {
        val response = BaseResponse<List<BannerData>>()
        return response
    }

}