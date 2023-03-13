package app.allever.android.lib.common.function.network

import app.allever.android.lib.common.function.network.reponse.BannerData
import app.allever.android.lib.core.function.network.HttpHelper
import app.allever.android.lib.core.function.network.ResponseCallback
import app.allever.android.lib.core.function.network.cache.ResponseCache
import app.allever.android.lib.network.ApiService
import app.allever.android.lib.network.RetrofitCallback
import kotlinx.coroutines.delay

object AppRepository {
    private val wanAndroidApi by lazy {
        ApiService.get(WanAndroidApi::class.java)
    }

    @Deprecated("")
    suspend fun getBannerForJava() = HttpHelper.requestForJava {
        wanAndroidApi.getBannerForJava()
    }

    @Deprecated("")
    suspend fun getBanner() = HttpHelper.request {
        wanAndroidApi.getBanner()
    }

    /**
     * Java回调方式
     */
    fun getBannerCall(
        responseCache: ResponseCache<*>? = null,
        callback: ResponseCallback<List<BannerData>>? = null
    ) {
        HttpHelper.enqueue(responseCache, callback) {
            wanAndroidApi.getBannerCall().enqueue(RetrofitCallback(responseCache, callback))
        }
    }

    /**
     * kotlin协程
     */
    suspend fun getBanner(responseCache: ResponseCache<*>? = null) =
        HttpHelper.request(responseCache) {
            wanAndroidApi.getBanner()
        }

    /**
     * kotlin协程 + LiveData 方式一
     */
    suspend fun getBannerForLiveData(responseCache: ResponseCache<*>? = null) =
        HttpHelper.requestLiveData(responseCache) {
            wanAndroidApi.getBanner()
        }

    /**
     * kotlin协程 + LiveData 方式一
     */
    fun getBannerWithLiveData(responseCache: ResponseCache<*>? = null) =
        HttpHelper.requestLiveData2(responseCache) {
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

    suspend fun getHomePageArticleList(page: Int) = HttpHelper.request {
        wanAndroidApi.getHomePageList(page)
    }
}