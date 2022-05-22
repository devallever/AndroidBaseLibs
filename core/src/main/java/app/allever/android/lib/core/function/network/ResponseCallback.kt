package app.allever.android.lib.core.function.network

import app.allever.android.lib.core.function.network.response.NetResponse

interface ResponseCallback<DATA> {
    fun onSuccess(response: NetResponse<DATA>)
    fun onFail(response: NetResponse<DATA>)
}