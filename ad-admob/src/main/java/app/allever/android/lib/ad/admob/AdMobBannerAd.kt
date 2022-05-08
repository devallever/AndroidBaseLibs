package app.allever.android.lib.ad.admob

import android.view.View
import android.view.ViewGroup
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.function.ad.chain.AdChainListener
import app.allever.android.lib.core.function.ad.widget.BannerLayout
import app.allever.android.lib.core.function.ad.chain.IAd
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class AdMobBannerAd: IAd() {
    private var mBannerView: AdView? = null
    override fun load(adPosition: String?, container: ViewGroup?, adListener: AdChainListener?) {

    }

    override fun show() {
    }

    override fun loadAndShow(
        adPosition: String?,
        container: ViewGroup?,
        adListener: AdChainListener?
    ) {
        mBannerView = AdView(App.context)
        mBannerView?.adSize = AdSize.SMART_BANNER
        mBannerView?.adUnitId = adPosition
        mBannerView?.adListener = object : com.google.android.gms.ads.AdListener() {
            override fun onAdFailedToLoad(i: Int) {
                super.onAdFailedToLoad(i)
                log("加载 AdMob Banner 失败, 错误码： $i")
                AdMobBusiness.logError(i)
                adListener?.onFailed("加载AdMob Banner 失败, 错误码： $i")
            }

            override fun onAdLoaded() {
                super.onAdLoaded()
                log("加载 AdMob Banner 成功")
                val bannerLayout = container as? BannerLayout
                val bannerContainer = bannerLayout?.bannerContainer
                if (bannerContainer != null) {
                    bannerContainer.addView(mBannerView)
                } else {
                    container?.addView(mBannerView)
                }
                
                container?.visibility = View.VISIBLE
                adListener?.onLoaded(this@AdMobBannerAd)
            }

            override fun onAdClosed() {
                super.onAdClosed()
                adListener?.onDismiss()
            }
        }

        //加载请求
        val reqBuild = AdRequest.Builder()
        AdMobBusiness.testDevicesList.map {
            reqBuild.addTestDevice(it)
        }
        try {
            mBannerView?.loadAd(reqBuild.build())
        } catch (e: Exception) {
            e.printStackTrace()
            log("请求报错 AdMob Banner：${e.message}")
            adListener?.onFailed("请求报错 AdMob Banner：${e.message}")
        } catch (e: Error) {
            e.printStackTrace()
            log("请求报错 AdMob Banner：${e.message}")
            adListener?.onFailed("请求报错 AdMob Banner：${e.message}")
        }

    }

    override fun destroy() {
        mBannerView?.destroy()
    }

    override fun onAdResume() {
        mBannerView?.resume()
    }

    override fun onAdPause() {
        mBannerView?.pause()
    }

}