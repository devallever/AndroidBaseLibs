package app.allever.android.lib.core.function.businessinterceptor

abstract class BaseInterceptor : Interceptor {

    protected var mChain: InterceptChain? = null

    override fun intercept(chain: InterceptChain) {
        mChain = chain
    }
}