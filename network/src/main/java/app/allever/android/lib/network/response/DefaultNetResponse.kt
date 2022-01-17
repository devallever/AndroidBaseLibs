package app.allever.android.lib.network.response

class DefaultNetResponse<DATA>(private var code: Int, private var msg: String) :
    NetResponse<DATA>() {
    override fun getCode() = code

    override fun getMsg() = msg

    override fun <T : NetResponse<DATA>> generateResponse(code: Int, msg: String, data: DATA?): T {
        val baseResponse = DefaultNetResponse<DATA>(code, msg)
        baseResponse.data = data
        return baseResponse as T
    }
}