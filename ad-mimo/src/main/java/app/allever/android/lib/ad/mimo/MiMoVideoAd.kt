package app.allever.android.lib.ad.mimo

import android.view.ViewGroup
import app.allever.android.lib.core.function.ad.chain.AdChainListener
import app.allever.android.lib.core.function.ad.chain.IAd
import com.miui.zeus.mimo.sdk.ad.IRewardVideoAdWorker

class MiMoVideoAd: IAd() {

    private var mAdWorker: IRewardVideoAdWorker? = null

    override fun load(adPosition: String?, container: ViewGroup?, adListener: AdChainListener?) {
        mAdWorker = MiMoBusiness.loadRewardVideo(adPosition!!, object : AdChainListener {
            override fun onLoaded(ad: IAd?) {
                adListener?.onLoaded(this@MiMoVideoAd)
            }

            override fun onFailed(msg: String) {
                adListener?.onFailed(msg)
            }

            override fun onShowed() {
                adListener?.onShowed()
            }

            override fun onDismiss() {
                adListener?.onDismiss()
            }

        })
    }

    override fun show() {
        MiMoBusiness.showVideo(mAdWorker)
    }

    override fun loadAndShow(
        adPosition: String?,
        container: ViewGroup?,
        adListener: AdChainListener?
    ) {
    }

    override fun destroy() {
        MiMoBusiness.destroyVideo(mAdWorker)
    }

    override fun onAdResume() {

    }

    override fun onAdPause() {

    }
}