package app.allever.android.lib.core.function.network.response

abstract class NetResponse<DATA> {
    var data: DATA? = null

    abstract fun getCode(): Int
    abstract fun getMsg(): String

    abstract fun setData(code: Int, msg: String, data: DATA?)

//    abstract fun <T : NetResponse<DATA>> generateResponse(code: Int, msg: String, data: DATA?): T
}