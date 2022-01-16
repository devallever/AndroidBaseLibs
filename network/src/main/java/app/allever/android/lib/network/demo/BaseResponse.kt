package app.allever.android.lib.network.demo

import app.allever.android.lib.network.response.NetResponse

open class BaseResponse<DATA> : NetResponse<DATA>() {
    var errorCode: Int = 0
    var errorMsg: String = ""

    override fun getCode() = errorCode
    override fun getMsg() = errorMsg

    override fun <T : NetResponse<DATA>> generateResponse(code: Int, msg: String, data: DATA?): T {
        val baseResponse = BaseResponse<DATA>()
        baseResponse.errorCode = code
        baseResponse.errorMsg = msg
        baseResponse.data = data
        return baseResponse as T
    }
}