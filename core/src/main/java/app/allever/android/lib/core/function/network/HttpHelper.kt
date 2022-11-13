package app.allever.android.lib.core.function.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.logE
import app.allever.android.lib.core.ext.toast
import app.allever.android.lib.core.function.network.cache.ResponseCache
import app.allever.android.lib.core.function.network.exception.ExceptionHandle
import app.allever.android.lib.core.function.network.exception.HttpException
import app.allever.android.lib.core.function.network.response.DefaultNetResponse
import app.allever.android.lib.core.function.network.response.NetResponse
import app.allever.android.lib.core.helper.GsonHelper
import app.allever.android.lib.core.helper.NetworkHelper

object HttpHelper {
    @Deprecated("Java调用直接用回调就好了")
    inline fun <T : NetResponse<*>> requestForJava(block: () -> T): T? {
        return try {
            val response = block()
            response
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    inline fun <T : NetResponse<*>> request(block: () -> T): T {
        return request(null, block)
    }


    /**
     * kotlin协程方式请求
     * @param responseClz 响应体的class类型
     * @param responseCache 缓存类，null表示不使用缓存
     * @param block 高阶函数，执行相应都网络请求
     */
    inline fun <T : NetResponse<*>> request(
        responseCache: ResponseCache<*>? = null,
        block: () -> T
    ): T {
        try {
            if (!NetworkHelper.isNetworkAvailable(App.context) || responseCache != null) {
                val response = responseCache?.getCache<T>()
                response?.let {
                    log("使用缓存: ${response.data}")
                    return response
                }
            }

            val response = block()
            if (isSuccessCode(response.getCode())) {
                responseCache?.cacheResponse(response)
            }
            return response
        } catch (e: Exception) {
            e.printStackTrace()
            logE(e.message)
            val exception = ExceptionHandle.handleException(e)
            val response = HttpConfig.baseResponseClz.newInstance()
            log("responseClz = ${response?.javaClass?.simpleName}")
            if (response is NetResponse<*>) {
                response.setData(exception.code, exception.message ?: "", null)
            }
            return response as T
        }
    }

    /**
     * kotlin协程方式请求
     * @param responseClz 响应体的class类型
     * @param responseCache 缓存类，null表示不使用缓存
     * @param block 高阶函数，执行相应都网络请求
     */
    inline suspend fun <T : NetResponse<*>> requestLiveData(
        responseCache: ResponseCache<*>? = null,
        block: () -> T
    ): LiveData<T> {
        val liveData = MutableLiveData<T>()
        liveData.value = request(responseCache, block)
        return liveData
    }

    /**
     * kotlin协程方式请求
     * @param responseClz 响应体的class类型
     * @param responseCache 缓存类，null表示不使用缓存
     * @param block 高阶函数，执行相应都网络请求
     */
    inline fun <T : NetResponse<*>> requestLiveData2(
        responseCache: ResponseCache<*>? = null,
        crossinline block: suspend () -> T
    ) = liveData {
        try {
            if (!NetworkHelper.isNetworkAvailable(App.context) || responseCache != null) {
                val response = responseCache?.getCache<T>()
                response?.let {
                    log("使用缓存: ${response.data}")
                    emit(it)
                    return@liveData
                }
            }

            val response = block()
            if (isSuccessCode(response.getCode())) {
                responseCache?.cacheResponse(response)
            }
            emit(response)
        } catch (e: Exception) {
            e.printStackTrace()
            logE(e.message)
            val exception = ExceptionHandle.handleException(e)
            val response = HttpConfig.baseResponseClz.newInstance()
            log("responseClz = ${response?.javaClass?.simpleName}")
            if (response is NetResponse<*>) {
                response.setData(exception.code, exception.message ?: "", null)
            }
            emit(response as T)
        }
    }

    /**
     * java回调方式请求
     */
    fun <DATA, R : NetResponse<DATA>> enqueue(
        responseCache: ResponseCache<*>? = null,
        callback: ResponseCallback<DATA>? = null,
        requestTask: Runnable? = null
    ) {
        if (!NetworkHelper.isNetworkAvailable(App.context) || responseCache != null) {
            val response = responseCache?.getCache<R>()
            response?.let {
                log("使用缓存: ${GsonHelper.toJson(response)}")
                callback?.onSuccess(response)
                return
            }
        }

        requestTask?.run()
    }

    fun <T> showMessageIfFail(result: Result<T>) {
        if (result.isFailure) {
            toast((result.exceptionOrNull() as? HttpException)?.response()?.getMsg())
        }
    }

    fun <DATA> getJson(result: Result<DATA>): String {
        return GsonHelper.toJson(result.getOrNull())
    }

    fun isSuccessCode(code: Int): Boolean {
        return code == HttpConfig.successCode
    }

    fun <DATA, R : NetResponse<DATA>> handleSuccessCallback(
        responseCache: ResponseCache<*>? = null,
        requestResult: R? = null,
        callback: ResponseCallback<DATA>? = null
    ) {
        if (requestResult == null) {
            val defaultResponse = DefaultNetResponse<DATA>(-1, "没有返回数据")
            callback?.onFail(defaultResponse)
            return
        }

        callback?.onSuccess(requestResult)

        if (isSuccessCode(requestResult.getCode())) {
            responseCache?.cacheResponse(requestResult)
        }
    }

    fun <DATA> handleFailCallback(callback: ResponseCallback<DATA>? = null, t: Throwable? = null) {
        val exception = ExceptionHandle.handleException(t)
        val defaultNetResponse =
            exception.message?.let { DefaultNetResponse<DATA>(exception.code, it) }
        defaultNetResponse?.let { callback?.onFail(it) }
    }
}