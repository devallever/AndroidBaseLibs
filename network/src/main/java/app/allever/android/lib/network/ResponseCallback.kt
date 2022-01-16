package app.allever.android.lib.network

import app.allever.android.lib.network.response.NetResponse

interface ResponseCallback<DATA> {
    fun onSuccess(code: Int, msg: String, data: DATA?)
    fun onFail(code: Int, msg: String, response: NetResponse<DATA>?)
}