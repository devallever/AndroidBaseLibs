package app.allever.android.lib.network

import app.allever.android.lib.core.loge
import app.allever.android.lib.core.toast
import app.allever.android.lib.network.response.NetResponse

abstract class NetworkHandler {

    inline fun <DATA : NetResponse<*>> request(block: () -> DATA): Result<DATA> {
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

    fun <DATA> showMessageIfFail(result: Result<DATA>) {
        if (result.isFailure) {
            toast((result.exceptionOrNull() as? HttpException)?.response?.getMsg())
        }
    }

    fun <DATA> getJson(result: Result<DATA>): String {
        return GsonHelper.toJson(result.getOrNull())
    }
}