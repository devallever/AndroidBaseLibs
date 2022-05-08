package app.allever.android.lib.core.function.ad.chain

interface AdChainListener {
    fun onLoaded(ad: IAd?)

    fun onFailed(msg: String)

    fun onShowed()

    fun onDismiss()

    fun playEnd() {}

    fun onStimulateSuccess() {}

    fun onClick() {}
}