package app.allever.android.lib.network

import app.allever.android.lib.core.ext.loge
import app.allever.android.lib.network.response.DefaultNetResponse
import app.allever.android.lib.network.response.NetResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DefaultRetrofitCallback<DATA, R : NetResponse<DATA>> : Callback<R> {

    private var responseCallback: ResponseCallback<DATA>

    constructor(responseCallback: ResponseCallback<DATA>) {
        this.responseCallback = responseCallback
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
            val defaultResponse = DefaultNetResponse<DATA>(HttpCode.UNKNOW, "没有返回数据")
            callback.onFail(defaultResponse)
            return
        }
        val code: Int = requestResult.getCode()
        val msg: String = requestResult.getMsg()
        if (HttpCode.isSuccessCode(code)) {
            callback.onSuccess(requestResult)
        } else {
            val defaultResponse = DefaultNetResponse<DATA>(code, msg)
            callback.onFail(defaultResponse)
        }
    }

    private fun <DATA> handleFail(callback: ResponseCallback<DATA>?, t: Throwable?) {
        if (callback == null) {
            return
        }
        var msg = ""
        if (t != null) {
            loge(0.toString() + " -> " + t.message)
            msg = t.message ?: ""
        }
        val defaultNetResponse = DefaultNetResponse<DATA>(HttpCode.UNKNOW, msg)
        callback.onFail(defaultNetResponse)
    }

    override fun onResponse(call: Call<R>, response: Response<R>) {
        handleSuccess(response, responseCallback)
    }

    override fun onFailure(call: Call<R>, t: Throwable) {
        handleFail(responseCallback, t)
    }
}