//package app.allever.android.lib.ad.admob
//
//import android.view.ViewGroup
//import com.google.android.gms.ads.AdRequest
//import com.google.android.gms.ads.AdSize
//import com.google.android.gms.ads.AdView
//
//@Deprecated("")
//class AdMobBanner: BaseAd() {
//    private var mBannerView: AdView? = null
//    override fun load(adPosition: String, container: ViewGroup?, adListener: AdListener?) {
//
//    }
//
//    override fun show() {
//    }
//
//    override fun loadAndShow(adPosition: String, container: ViewGroup?, adListener: AdListener?) {
//        mBannerView = AdView(App.context)
//        mBannerView?.adSize = AdSize.SMART_BANNER
//        mBannerView?.adUnitId = adPosition
//        mBannerView?.adListener = object : com.google.android.gms.ads.AdListener() {
//            override fun onAdFailedToLoad(i: Int) {
//                super.onAdFailedToLoad(i)
//                adListener?.onFailed()
//                log("加载AdMob Banner 失败, 错误码： $i")
//            }
//
//            override fun onAdLoaded() {
//                super.onAdLoaded()
//                log("加载AdMob Banner 成功")
//                container?.addView(mBannerView)
//            }
//        }
//
//        //加载请求
//        val reqBuild = AdRequest.Builder()
//        AdMobHelper.testDevicesList.map {
//            reqBuild.addTestDevice(it)
//        }
//        mBannerView?.loadAd(reqBuild.build())
//    }
//
//    override fun destroy() {
//        mBannerView?.destroy()
//    }
//}