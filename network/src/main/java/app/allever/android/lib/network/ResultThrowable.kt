package app.allever.android.lib.network

import java.lang.Exception

/**
 * @ClassName RsThrowable
 * @Description TODO
 * @Author Jerry
 * @Date 2020/5/4 0:17
 * @Version 1.0
 */
class ResultThrowable : Exception {
    var code: Int
    override var message: String? = null

    constructor(cause: Throwable?, code: Int) : super(cause) {
        this.code = code
    }

    constructor(code: Int, message: String?) {
        this.code = code
        this.message = message
    }
}