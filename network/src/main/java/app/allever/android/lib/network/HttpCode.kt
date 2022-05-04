package app.allever.android.lib.network

class HttpCode {
    companion object {
        fun isSuccessCode(code: Int) = code == HttpConfig.successCode
    }
}