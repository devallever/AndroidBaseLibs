package app.allever.android.lib.network.response

open class BaseResponse<T>() {
    var `data`: T? = null
    var errorCode: Int = 0
    var errorMsg: String = ""
}