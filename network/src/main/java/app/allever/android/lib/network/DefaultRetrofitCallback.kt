package app.allever.android.lib.network

import app.allever.android.lib.core.function.network.exception.ExceptionHandle
import app.allever.android.lib.core.function.network.ResponseCallback
import app.allever.android.lib.core.function.network.cache.ResponseCache
import app.allever.android.lib.core.function.network.response.DefaultNetResponse
import app.allever.android.lib.core.function.network.response.NetResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DefaultRetrofitCallback<DATA, R : NetResponse<DATA>> : Callback<R> {

    private var responseCallback: ResponseCallback<DATA>
    private var responseCache: ResponseCache<*>? = null

    constructor(responseCallback: ResponseCallback<DATA>) {
        this.responseCallback = responseCallback
    }

    constructor(responseCache: ResponseCache<*>?, responseCallback: ResponseCallback<DATA>) {
        this.responseCallback = responseCallback
        this.responseCache = responseCache
    }

    private fun handleSuccess(
        response: Response<R>,
        callback: ResponseCallback<DATA>?
    ) {
        if (callback == null) {
            return
        }
        val requestResult: R? = response.body()
        if (requestResult == null) {
            val defaultResponse = DefaultNetResponse<DATA>(-1, "没有返回数据")
            callback.onFail(defaultResponse)
            return
        }

        callback.onSuccess(requestResult)

        if (HttpCode.isSuccessCode(requestResult.getCode())) {
            responseCache?.cacheResponse(requestResult)
        }

        //返回其他码也有可能成功
//        val code: Int = requestResult.getCode()
//        val msg: String = requestResult.getMsg()
//        if (HttpCode.isSuccessCode(code)) {
//            callback.onSuccess(requestResult)
//        } else {
//            val defaultResponse = DefaultNetResponse<DATA>(code, msg)
//            callback.onFail(defaultResponse)
//        }
    }

    private fun <DATA> handleFail(callback: ResponseCallback<DATA>?, t: Throwable?) {
        if (callback == null) {
            return
        }
        val exception = ExceptionHandle.handleException(t)
        val defaultNetResponse =
            exception.message?.let { DefaultNetResponse<DATA>(exception.code, it) }
        defaultNetResponse?.let { callback.onFail(it) }
    }

    override fun onResponse(call: Call<R>, response: Response<R>) {
        handleSuccess(response, responseCallback)
    }

    override fun onFailure(call: Call<R>, t: Throwable) {
        handleFail(responseCallback, t)
    }
}