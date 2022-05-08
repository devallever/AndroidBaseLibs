package app.allever.android.lib.core.function.ad.chain

import android.view.ViewGroup
import app.allever.android.lib.core.function.ad.AdListener

abstract class IBannerAd : IAd() {
    override fun load(adPosition: String?, container: ViewGroup?, adListener: AdChainListener?) {

    }

    override fun show() {

    }

    abstract fun loadAndShow(adPosition: String?, container: ViewGroup?, adListener: AdListener?)

}