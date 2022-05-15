package com.allever.lib.ad.mimo

import android.view.ViewGroup
import app.allever.android.lib.ad.mimo.MiMoAdHelper
import app.allever.android.lib.core.function.ad.AdListener
import app.allever.android.lib.core.function.ad.BaseAd
import com.miui.zeus.mimo.sdk.ad.IAdWorker
import com.xiaomi.ad.common.pojo.AdType

/***
 * 米盟通用Ad
 */
open class MiMoAd(private val adType: AdType) : BaseAd() {

    private var mAdWorker: IAdWorker? = null

    override fun load(adPosition: String, container: ViewGroup?, adListener: AdListener?) {
        mAdWorker = MiMoAdHelper.load(adType, adPosition, container, adListener)
        return
    }

    override fun show() {
        MiMoAdHelper.show(mAdWorker)
    }

    override fun loadAndShow(adPosition: String, container: ViewGroup?, adListener: AdListener?) {
        mAdWorker = MiMoAdHelper.loadAndShow(adType, adPosition, container, adListener)
    }

    override fun destroy() {
        MiMoAdHelper.destroy(mAdWorker)
    }
}