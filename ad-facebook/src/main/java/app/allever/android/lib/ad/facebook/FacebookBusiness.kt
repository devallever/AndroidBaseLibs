package app.allever.android.lib.ad.facebook

import android.content.Context
import app.allever.android.lib.core.function.ad.chain.IAd
import app.allever.android.lib.core.function.ad.chain.IAdBusiness
import com.facebook.ads.AudienceNetworkAds

object FacebookBusiness : IAdBusiness() {

    override fun init(context: Context, appId: String, appKey: String, appToken: String) {
        AudienceNetworkAds.initialize(context)

//        AdSettings.addTestDevice("96bb3dea-f978-43dd-a244-85311c1ed158")//nexus5x
//        AdSettings.addTestDevice("744c0fe5-4a13-4d06-b61a-659d87446209")//pixel
//        AdSettings.addTestDevice("0c064906-5fad-4ffe-8859-b3065121cda0")//pixel
//        AdSettings.addTestDevice("80ce9a1d-d4af-43e9-9e75-f2c46c8777e4")//pixel
    }

    override fun createBannerAd(): IAd? = FacebookBannerAd()

    override fun createInsertAd(): IAd? = FacebookInsertAd()

    override fun createVideoAd(): IAd? = null

    override fun createDownloadAd(): IAd? = null

    override fun createNativeAd(adType: String): IAd? = null

    override fun destroy(context: Context) {
    }
}