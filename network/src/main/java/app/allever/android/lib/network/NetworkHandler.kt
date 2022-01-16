package app.allever.android.lib.network

import app.allever.android.lib.core.loge
import app.allever.android.lib.core.toast
import app.allever.android.lib.network.response.NetResponse
import retrofit2.Response

abstract class NetworkHandler {


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

    fun <T> showMessageIfFail(result: Result<T>) {
        if (result.isFailure) {
            toast((result.exceptionOrNull() as? HttpException)?.response?.getMsg())
        }
    }

    fun <DATA> getJson(result: Result<DATA>): String {
        return GsonHelper.toJson(result.getOrNull())
    }

    private fun <DATA> handleSuccess(
        response: Response<NetResponse<DATA>>,
        callback: ResponseCallback<DATA>?
    ) {
        if (callback == null) {
            return
        }
        val requestResult: NetResponse<DATA>? = response.body()
        if (requestResult == null) {
            callback.onFail(HttpCode.UNKNOW, "没有返回数据", null)
            return
        }
        val code: Int = requestResult.getCode()
        val msg: String = requestResult.getMsg()
        var message = msg
        if (HttpCode.isSuccessCode(code)) {
            val data: DATA? = requestResult.data
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
}