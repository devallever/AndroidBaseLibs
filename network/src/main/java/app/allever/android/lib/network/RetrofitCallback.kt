package app.allever.android.lib.network

import app.allever.android.lib.core.function.network.NetworkHandler
import app.allever.android.lib.core.function.network.ResponseCallback
import app.allever.android.lib.core.function.network.cache.ResponseCache
import app.allever.android.lib.core.function.network.response.NetResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetrofitCallback<DATA, R : NetResponse<DATA>> : Callback<R>, NetworkHandler {

    private var responseCallback: ResponseCallback<DATA>
    private var responseCache: ResponseCache<*>? = null

    constructor(responseCallback: ResponseCallback<DATA>) {
        this.responseCallback = responseCallback
    }

    constructor(responseCache: ResponseCache<*>?, responseCallback: ResponseCallback<DATA>) {
        this.responseCallback = responseCallback
        this.responseCache = responseCache
    }

    override fun onResponse(call: Call<R>, response: Response<R>) {
        handleSuccessCallback(responseCache, response.body(), responseCallback)
    }

    override fun onFailure(call: Call<R>, t: Throwable) {
        handleFailCallback(responseCallback, t)
    }
}