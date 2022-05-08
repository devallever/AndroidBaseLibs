package app.allever.android.lib.core.function.ad

@Deprecated("使用adChain")
interface AdListener {
    fun onLoaded()

    fun onShowed()

    fun onDismiss()

    fun onFailed()

    fun playEnd() {}

    fun onStimulateSuccess() {}

    fun onClick() {}
}