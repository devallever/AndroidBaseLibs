package app.allever.android.lib.network.demo

import app.allever.android.lib.core.function.network.HttpConfig

internal class HttpCode {
    companion object {
        fun isSuccessCode(code: Int) = code == HttpConfig.successCode
    }
}