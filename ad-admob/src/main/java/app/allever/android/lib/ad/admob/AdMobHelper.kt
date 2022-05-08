//package app.allever.android.lib.ad.admob
//
//import android.content.Context
//import app.allever.android.lib.core.ext.log
//import app.allever.android.lib.core.function.ad.AdManager
//import app.allever.android.lib.core.function.ad.BaseAd
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.MobileAds
//
//object AdMobHelper: AdManager() {
//    var testDevicesList = mutableListOf<String>()
//    override fun init(context: Context, appId: String, appKey: String, appToken: String) {
//        MobileAds.initialize(context) {
//
//        }
////        testDevicesList.add("811A5A5DA1BF1E2FC9EE39041EC322FF")
////        testDevicesList.add("1621DB3C172AE6711BA840F4AEF6EF48")
//    }
//
//    override fun createBannerAd(): BaseAd? {
//        return AdMobBanner()
//    }
//
//    override fun createInsertAd(): BaseAd? {
//        return AdMobInsert()
//    }
//
//    override fun createVideoAd(): BaseAd? = AdMobVideo()
//
//    override fun createDownloadAd(): BaseAd? = null
//
//    override fun destroy(context: Context) {
//    }
//
//    fun createAdRequest(): AdRequest {
//        //加载请求
//        val reqBuild = AdRequest.Builder()
//        for (device in testDevicesList) {
//            reqBuild.addTestDevice(device)
//        }
//        return reqBuild.build()
//    }
//
//    internal fun logError(errorCode:  Int) {
//        when (errorCode) {
//            0 -> {
//                log("内部错误")
//            }
//            1 -> {
//                log("")
//            }
//            2 -> {
//                log("网络异常")
//            }
//            3 -> {
//                log("没填充")
//            }
//        }
//    }
//
//}