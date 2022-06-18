package app.allever.android.lib.core.function.businessinterceptor

import app.allever.android.lib.core.function.businessinterceptor.InterceptChain
import app.allever.android.lib.core.function.businessinterceptor.Interceptor

abstract class BaseInterceptImpl : Interceptor {

    protected var mChain: InterceptChain? = null

    override fun intercept(chain: InterceptChain) {
        mChain = chain
    }
}