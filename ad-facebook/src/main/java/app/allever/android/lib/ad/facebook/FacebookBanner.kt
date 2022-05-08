//package com.allever.lib.ad.facebook
//
//import android.view.ViewGroup
//import com.allever.lib.ad.AdListener
//import com.allever.lib.ad.BaseAd
//import com.allever.lib.common.app.App
//import com.allever.lib.common.util.log
//import com.facebook.ads.Ad
//import com.facebook.ads.AdError
//import com.facebook.ads.AdSize
//import com.facebook.ads.AdView
//
//
//@Deprecated("")
//class FacebookBanner : BaseAd() {
//
//    private var facebookbanner: AdView? = null
//
//    override fun load(adPosition: String, container: ViewGroup?, adListener: AdListener?) {
//
//    }
//
//    override fun show() {
//    }
//
//    override fun loadAndShow(adPosition: String, container: ViewGroup?, adListener: AdListener?) {
//        facebookbanner = AdView(App.context, adPosition, AdSize.BANNER_320_50)
//        facebookbanner?.setAdListener(object : com.facebook.ads.AdListener {
//            override fun onError(ad: Ad, adError: AdError) {
//                adListener?.onFailed()
//                log("加载Facebook banner 失败 code = ${adError.errorCode}, message: ${adError.errorMessage}")
//            }
//
//            override fun onAdLoaded(ad: Ad) {
//                log("加载Facebook banner 成功")
//                adListener?.onLoaded()
//                container?.addView(facebookbanner)
//            }
//
//            override fun onAdClicked(ad: Ad) {
//                log("Facebook banner 被点击")
//            }
//
//            override fun onLoggingImpression(ad: Ad) {
//
//            }
//        })
//        facebookbanner?.loadAd()
//    }
//
//    override fun destroy() {
//        facebookbanner?.destroy()
//    }
//}