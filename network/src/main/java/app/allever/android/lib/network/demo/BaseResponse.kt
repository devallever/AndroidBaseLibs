package app.allever.android.lib.network.demo

import app.allever.android.lib.network.response.NetResponse

open class BaseResponse<DATA> : NetResponse<DATA>() {
    var errorCode: Int = 0
    var errorMsg: String = ""

    override fun getCode() = errorCode
    override fun getMsg() = errorMsg
}