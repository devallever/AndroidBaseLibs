package app.allever.android.lib.network

import app.allever.android.lib.network.response.NetResponse

class HttpException(val response: NetResponse<*>) : RuntimeException() {
}