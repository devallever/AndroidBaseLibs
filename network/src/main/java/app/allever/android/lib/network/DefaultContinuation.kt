package app.allever.android.lib.network

import app.allever.android.lib.network.response.NetResponse
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext

abstract class DefaultContinuation<T: NetResponse<*>> : Continuation<T> {
    override val context: CoroutineContext
        get() = Dispatchers.IO

    override fun resumeWith(result: Result<T>) {
        val response = result.getOrNull() as? NetResponse<T>
        if (response == null) {
            onFail(HttpCode.UNKNOW, "未知错误")
            return
        }

        val code = response.getCode()
        val msg = response.getMsg()
        if (HttpCode.isSuccessCode(code)) {
            onSuccess(result.getOrNull()!!)
        } else {
            onFail(code, msg)
        }
    }


    abstract fun onSuccess(response: T)
    abstract fun onFail(code: Int, msg: String)
}