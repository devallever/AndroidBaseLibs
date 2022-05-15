package app.allever.android.lib.core.function.ad.chain

import android.view.ViewGroup

abstract class IAd {
    abstract fun load(adPosition: String?, container: ViewGroup?, adListener: AdChainListener?)
    abstract fun show()
    abstract fun loadAndShow(
        adPosition: String?,
        container: ViewGroup?,
        adListener: AdChainListener?
    )

    abstract fun destroy()
    abstract fun onAdResume()
    abstract fun onAdPause()
}