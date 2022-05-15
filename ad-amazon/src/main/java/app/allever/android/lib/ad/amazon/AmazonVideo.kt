package app.allever.android.lib.ad.amazon

import android.view.ViewGroup
import app.allever.android.lib.core.function.ad.AdListener
import app.allever.android.lib.core.function.ad.BaseAd

class AmazonVideo : BaseAd() {
    override fun load(adPosition: String, container: ViewGroup?, adListener: AdListener?) {
        adListener?.onFailed()
    }

    override fun show() {
    }

    override fun loadAndShow(adPosition: String, container: ViewGroup?, adListener: AdListener?) {
        adListener?.onLoaded()
    }

    override fun destroy() {
    }
}