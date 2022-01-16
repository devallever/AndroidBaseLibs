package app.allever.android.lib.network.response

abstract class NetResponse<DATA> {
    var `data`: DATA? = null

    abstract fun getCode(): Int
    abstract fun getMsg(): String
}