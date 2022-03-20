package app.allever.android.lib.network

import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.helper.NetworkHelper
import app.allever.android.lib.core.log
import app.allever.android.lib.core.loge
import app.allever.android.lib.core.toast
import app.allever.android.lib.network.cache.ResponseCache
import app.allever.android.lib.network.response.NetResponse

abstract class NetworkHandler {


    @Deprecated("Java调用直接用回调就好了")
    inline fun <T : NetResponse<*>> requestForJava(block: () -> T): T? {
        val result: T
        return try {
            val response = block()
            response
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    inline fun <T : NetResponse<*>> request(block: () -> T): Result<T> {
        return try {
            val response = block()
            if (HttpCode.isSuccessCode(response.getCode())) {
                Result.success(response)
            } else {
                Result.failure(HttpException(response))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            loge(e.message)
            Result.failure(e)
        }
    }

    inline fun <T : NetResponse<*>> request(
        responseCache: ResponseCache<*>? = null,
        block: () -> T
    ): T? {
        try {
            if (!NetworkHelper.isNetworkAvailable(App.context) || responseCache != null) {
                val response = responseCache?.getCache<T>()
                response?.let {
                    log("使用缓存: ${response.data}")
                    return response
                }
            }

            val response = block()
            responseCache?.cacheResponse(response)
            return response
        } catch (e: Exception) {
            e.printStackTrace()
            loge(e.message)
            return null
        }
    }

    fun <T> showMessageIfFail(result: Result<T>) {
        if (result.isFailure) {
            toast((result.exceptionOrNull() as? HttpException)?.response?.getMsg())
        }
    }

    fun <DATA> getJson(result: Result<DATA>): String {
        return GsonHelper.toJson(result.getOrNull())
    }
}