package app.allever.android.lib.ad.mimo

import android.view.ViewGroup
import app.allever.android.lib.core.function.ad.AdListener
import app.allever.android.lib.core.function.ad.BaseAd
import com.miui.zeus.mimo.sdk.ad.IRewardVideoAdWorker

class MiMoVideo: BaseAd() {
    private var mAdWorker: IRewardVideoAdWorker? = null
    override fun load(adPosition: String, container: ViewGroup?, adListener: AdListener?) {
        mAdWorker = MiMoAdHelper.loadRewardVideo(adPosition, adListener)
    }

    override fun show() {
        MiMoAdHelper.showVideo(mAdWorker)
    }

    override fun loadAndShow(adPosition: String, container: ViewGroup?, adListener: AdListener?) {
//        MiMoAdHelper.loadRewardVideo()
    }

    override fun destroy() {
        MiMoAdHelper.destroyVideo(mAdWorker)
    }
}