package app.allever.android.lib.network

import app.allever.android.lib.core.function.network.HttpHelper
import app.allever.android.lib.core.function.network.ResponseCallback
import app.allever.android.lib.core.function.network.cache.ResponseCache
import app.allever.android.lib.core.function.network.response.NetResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

internal class RetrofitCallback<DATA, R : NetResponse<DATA>> : Callback<R> {

    private var responseCallback: ResponseCallback<DATA>? = null
    private var responseCache: ResponseCache<*>? = null

    constructor(responseCallback: ResponseCallback<DATA>?) {
        this.responseCallback = responseCallback
    }

    constructor(responseCache: ResponseCache<*>? = null, responseCallback: ResponseCallback<DATA>? = null) {
        this.responseCallback = responseCallback
        this.responseCache = responseCache
    }

    override fun onResponse(call: Call<R>, response: Response<R>) {
        HttpHelper.handleSuccessCallback(responseCache, response.body(), responseCallback)
    }

    override fun onFailure(call: Call<R>, t: Throwable) {
        HttpHelper.handleFailCallback(responseCallback, t)
    }
}