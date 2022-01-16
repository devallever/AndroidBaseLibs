package app.allever.android.lib.network.response

class DefaultNetResponse<DATA>: NetResponse<DATA>() {
    private var msg: String = ""
    private var code: Int = 0
    override fun getCode() = code

    override fun getMsg() = msg

    override fun <T : NetResponse<DATA>> generateResponse(code: Int, msg: String, data: DATA?): T {
        val baseResponse = DefaultNetResponse<DATA>()
        baseResponse.code = code
        baseResponse.msg = msg
        baseResponse.data = data
        return baseResponse as T
    }
}