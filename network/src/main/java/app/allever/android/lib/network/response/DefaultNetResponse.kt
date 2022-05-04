package app.allever.android.lib.network.response

class DefaultNetResponse<DATA>(private var code: Int, private var msg: String) :
    NetResponse<DATA>() {
    override fun getCode() = code

    override fun getMsg() = msg

    override fun setData(code: Int, msg: String, data: DATA?) {
        this.code = code
        this.msg = msg
        this.data = data
    }

//    override fun <T : NetResponse<DATA>> generateResponse(code: Int, msg: String, data: DATA?): T {
//        this.code = code
//        this.msg = msg
//        return this as T
//    }
}