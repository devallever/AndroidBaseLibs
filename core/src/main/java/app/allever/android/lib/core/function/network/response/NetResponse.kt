package app.allever.android.lib.core.function.network.response

import app.allever.android.lib.core.function.network.HttpConfig

abstract class NetResponse<DATA> {
    var data: DATA? = null

    abstract fun getCode(): Int
    abstract fun getMsg(): String

    abstract fun setData(code: Int, msg: String, data: DATA?)

    /**
     * 只能判断基础成功，某些情况下不是successCode, 也要处理相应的业务
     */
    fun success(): Boolean = getCode() == HttpConfig.successCode

//    abstract fun <T : NetResponse<DATA>> generateResponse(code: Int, msg: String, data: DATA?): T
}