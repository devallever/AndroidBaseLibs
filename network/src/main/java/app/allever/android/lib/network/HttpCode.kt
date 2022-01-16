package app.allever.android.lib.network

class HttpCode {
    companion object {
        val CODE_200 = 200
        val CODE_0 = 0

        fun isSuccessCode(code: Int) = code == CODE_0 || code == CODE_200
    }
}