package app.allever.android.lib.ad.admob

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.function.ad.ADType
import app.allever.android.lib.core.function.ad.chain.AdChainListener
import app.allever.android.lib.core.function.ad.chain.IAd
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.UnifiedNativeAd

class AdMobNativeAd(var adType: String): IAd() {

    private var nativeAdView: TemplateView? = null
    private var unifiedNativeAd: UnifiedNativeAd? = null
    private var mContainer: ViewGroup? = null

//    override fun load(adPosition: String?, container: ViewGroup?, adListener: AdChainListener?) {
//        nativeAdView = LayoutInflater.from(App.context).inflate(R.layout.common_native_layout, container) as UnifiedNativeAdView
////        nativeAdView
//
//        //加载请求
//        val reqBuild = AdRequest.Builder()
//        for (device in AdMobBusiness.testDevicesList) {
//            reqBuild.addTestDevice(device)
//        }
//        val adRequest = reqBuild.build()
//
//
//        val adLoader = AdLoader.Builder(App.context, adPosition)
//            .forUnifiedNativeAd { unifiedNativeAd ->
////                val styles = NativeTemplateStyle.Builder().build()
////                my_template.setStyles(styles)
////                my_template.setNativeAd(unifiedNativeAd)
////                nativeAdView = findViewById()
//                val mediaView = nativeAdView?.findViewById<MediaView>(R.id.mediaView)
//                mediaView?.setMediaContent(unifiedNativeAd.mediaContent)
//                
//                val iconView = nativeAdView?.findViewById<ImageView>(R.id.ivLogo)
//                val iconImage = unifiedNativeAd.icon
//                iconView?.setImageDrawable(iconImage.drawable)
//                nativeAdView?.iconView = iconView
//                
//                val headView = nativeAdView?.findViewById<TextView>(R.id.tvHeadLine)
//                headView?.text = unifiedNativeAd.headline
//                nativeAdView?.headlineView = headView
//                
//                val descView = nativeAdView?.findViewById<TextView>(R.id.tvDesc)
////                descView?.text = unifiedNativeAd.
////                adListener?.onLoaded(this)
//            }
//            .build()
//        adLoader.loadAd(adRequest)
//    }

    override fun load(adPosition: String?, container: ViewGroup?, adListener: AdChainListener?) {
        mContainer = container
        mContainer?.visibility = View.GONE
        val root = if (adType == ADType.NATIVE) {
            LayoutInflater.from(App.context).inflate(R.layout.native_ad_layout, container)
        } else {
            LayoutInflater.from(App.context).inflate(R.layout.native_ad_small_layout, container)
        }
        
        nativeAdView = root.findViewById(R.id.templateView)
        val styles = NativeTemplateStyle.Builder().build()
        nativeAdView?.setStyles(styles)

        //加载请求
        val reqBuild = AdRequest.Builder()
        for (device in AdMobBusiness.testDevicesList) {
            reqBuild.addTestDevice(device)
        }
        val adRequest = reqBuild.build()


        val adLoader = AdLoader.Builder(App.context, adPosition)
            .forUnifiedNativeAd { unifiedNativeAd ->
                log("加载 AdMob Native 成功")
                this.unifiedNativeAd = unifiedNativeAd
                adListener?.onLoaded(this)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(errorCode: Int) {
                    super.onAdFailedToLoad(errorCode)
                    log("加载 AdMob Native 失败, 错误码： $errorCode")
                    AdMobBusiness.logError(errorCode)
                    adListener?.onFailed("加载 AdMob Native 失败, 错误码： $errorCode")
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                    log("AdMob Native 消失")
                    adListener?.onDismiss()
                }
            })
            .build()
        adLoader.loadAd(adRequest)
    }

    override fun show() {
        mContainer?.visibility = View.VISIBLE
        nativeAdView?.setNativeAd(unifiedNativeAd)
    }

    override fun loadAndShow(
        adPosition: String?,
        container: ViewGroup?,
        adListener: AdChainListener?
    ) {
    }

    override fun destroy() {
        nativeAdView?.destroyNativeAd()
    }

    override fun onAdResume() {
    }

    override fun onAdPause() {
    }
}