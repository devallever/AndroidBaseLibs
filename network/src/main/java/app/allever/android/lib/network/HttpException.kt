package app.allever.android.lib.network

import app.allever.android.lib.network.response.NetResponse

@Deprecated("使用ExceptionHandle统一处理")
class HttpException(val response: NetResponse<*>) : RuntimeException() {
}