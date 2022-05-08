package app.allever.android.lib.ad.admob

import  android.content.Context
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.function.ad.chain.IAd
import app.allever.android.lib.core.function.ad.chain.IAdBusiness
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds

object AdMobBusiness : IAdBusiness() {

    var testDevicesList = mutableListOf<String>()

    override fun init(context: Context, appId: String, appKey: String, appToken: String) {
        MobileAds.initialize(context) {

        }

//        if (BuildConfig.DEBUG) {
//
//        }

        //这些设备在正式包中
//        testDevicesList.add("811A5A5DA1BF1E2FC9EE39041EC322FF")//Redmi 3- debug
//        testDevicesList.add("600A221E7BF159C0916DF2EF4B5A1C47")//red mi 6-debug
//        testDevicesList.add("B0162B5ADF3CAC55BC592B9517B3C036")//red mi 6 release
//        testDevicesList.add("CE7899C461F4234464DC088649441F31")//red mi 6 release
//        testDevicesList.add("A4BCA26EC3C53E6AFF89376C27008860")//nexus5x debug
//        testDevicesList.add("4FA39B8331A7933CCA98AC9B98253722")//nexus5x release
//        testDevicesList.add("932FB4C1EF910CFD258339400851B5F0")//nexus5x
//        testDevicesList.add("7A1F53AFFCE229CF4EE079CECC1041DB")//samsung 906k
        testDevicesList.add("291FE1C46E33FA2ACDF1614035E032D9")//pixel-release
        testDevicesList.add("3D96CBAF7AFB6D86E760D80F03735F23")//pixel-debug
    }

    override fun createBannerAd(): IAd? = AdMobBannerAd()

    override fun createInsertAd(): IAd? = AdMobInsertAd()

    override fun createVideoAd(): IAd? = AdMobVideoAd()

    override fun createDownloadAd(): IAd? = null

    override fun createNativeAd(adType: String): IAd? = AdMobNativeAd(adType)

    override fun destroy(context: Context) {
    }

    fun createAdRequest(): AdRequest {
        //加载请求
        val reqBuild = AdRequest.Builder()
        for (device in testDevicesList) {
            reqBuild.addTestDevice(device)
        }
        return reqBuild.build()
    }

    internal fun logError(errorCode:  Int) {
        when (errorCode) {
            0 -> {
                log("内部错误")
            }
            1 -> {
                log("")
            }
            2 -> {
                log("网络异常")
            }
            3 -> {
                log("没填充")
            }
        }
    }

}