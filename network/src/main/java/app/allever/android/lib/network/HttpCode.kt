package app.allever.android.lib.network

class HttpCode {
    companion object {
        const val CODE_200 = 200
        const val CODE_0 = 0

        const val UNKNOW = -1000

        fun isSuccessCode(code: Int) = code == CODE_0 || code == CODE_200
    }
}