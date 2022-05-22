package app.allever.android.lib.network

import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.loge
import app.allever.android.lib.core.ext.toast
import app.allever.android.lib.core.helper.NetworkHelper
import app.allever.android.lib.network.cache.ResponseCache
import app.allever.android.lib.network.demo.BaseResponse
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

    @Deprecated("没用缓存,弃用")
    inline fun <T : NetResponse<*>> request(block: () -> T): Result<T> {
        return try {
            val response = block()
            if (HttpCode.isSuccessCode(response.getCode())) {
                Result.success(response)
            } else {
                Result.failure(
                    HttpException(
                        response as BaseResponse<*>
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            loge(e.message)
            Result.failure(e)
        }
    }


    /**
     * kotlin协程方式请求
     * @param responseClz 响应体的class类型
     * @param responseCache 缓存类，null表示不使用缓存
     * @param block 高阶函数，执行相应都网络请求
     */
    inline fun <T : NetResponse<*>> request(
        responseClz: Class<*>?,
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
            if (HttpCode.isSuccessCode(response.getCode())) {
                responseCache?.cacheResponse(response)
            }
            return response
        } catch (e: Exception) {
            e.printStackTrace()
            loge(e.message)
            val exception = ExceptionHandle.handleException(e)
            val response = responseClz?.newInstance()
            log("responseClz = ${response?.javaClass?.simpleName}")
            if (response is NetResponse<*>) {
                response.setData(exception.code, exception.message ?: "", null)
            }
            return response as? T?
        }
    }

    /**
     * java回调方式请求
     */
    fun <DATA, R : NetResponse<DATA>> enqueue(
        responseCache: ResponseCache<*>? = null,
        callback: ResponseCallback<DATA>,
        requestTask: Runnable
    ) {
        if (!NetworkHelper.isNetworkAvailable(App.context) || responseCache != null) {
            val response = responseCache?.getCache<R>()
            response?.let {
                log("使用缓存: ${GsonHelper.toJson(response)}")
                callback.onSuccess(response)
                return
            }
        }

        requestTask.run()
    }

    @Deprecated("没处理异常时候的返回，弃用")
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
            toast((result.exceptionOrNull() as? HttpException)?.response()?.getMsg())
        }
    }

    fun <DATA> getJson(result: Result<DATA>): String {
        return GsonHelper.toJson(result.getOrNull())
    }
}