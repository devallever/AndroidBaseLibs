package app.allever.android.lib.ad.mimo

import android.view.ViewGroup
import app.allever.android.lib.core.function.ad.AdListener
import com.allever.lib.ad.mimo.MiMoAd
import com.xiaomi.ad.common.pojo.AdType

class MiMoBanner : MiMoAd(AdType.AD_BANNER) {
    override fun load(adPosition: String, container: ViewGroup?, adListener: AdListener?) {
        loadAndShow(adPosition, container, adListener)
    }
}