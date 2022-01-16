package app.allever.android.lib.network

import app.allever.android.lib.core.loge
import app.allever.android.lib.network.response.NetResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DefaultRetrofitCallback<DATA, R: NetResponse<DATA>> : Callback<R> {

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
            callback.onFail(HttpCode.UNKNOW, "没有返回数据", null)
            return
        }
        val code: Int = requestResult.getCode()
        val msg: String = requestResult.getMsg()
        var message = msg
        if (HttpCode.isSuccessCode(code)) {
            val data = requestResult.data
            callback.onSuccess(code, message, data)
        } else {
            callback.onFail(code, message, null)
        }
    }

    private fun <DATA> handleFail(callback: ResponseCallback<DATA>?, t: Throwable?) {
        if (callback == null) {
            return
        }
        var msg: String = ""
        if (t != null) {
            loge(0.toString() + " -> " + t.message)
            msg = t.message ?: ""
        }
        callback.onFail(0, msg, null)
    }

    override fun onResponse(call: Call<R>, response: Response<R>) {
        handleSuccess(response, responseCallback)
    }

    override fun onFailure(call: Call<R>, t: Throwable) {
        handleFail(responseCallback, t)
    }
}