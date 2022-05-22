package app.allever.android.lib.network

import app.allever.android.lib.core.function.network.HttpConfig

class HttpCode {
    companion object {
        fun isSuccessCode(code: Int) = code == HttpConfig.successCode
    }
}