package app.allever.android.lib.ad.facebook

import android.view.ViewGroup
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.function.ad.chain.AdChainListener
import app.allever.android.lib.core.function.ad.chain.IAd
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener

class FacebookInsertAd : IAd() {

    private var insertAd: InterstitialAd? = null

    override fun load(adPosition: String?, container: ViewGroup?, adListener: AdChainListener?) {
        insertAd = InterstitialAd(container?.context, adPosition)
        insertAd?.setAdListener(object : InterstitialAdListener {
            override fun onInterstitialDisplayed(ad: Ad?) {
                log("Facebook Insert 展示")
                adListener?.onShowed()
            }

            override fun onAdClicked(ad: Ad?) {
                log("Facebook InsertAd 被点击")
                adListener?.onClick()
            }

            override fun onInterstitialDismissed(ad: Ad?) {
                log("Facebook InsertAd 消失")
                adListener?.onDismiss()
            }

            override fun onError(ad: Ad?, error: AdError?) {
                log("加载 Facebook Insert 失败, 错误码： ${error?.errorCode} - ${error?.errorMessage}")
                adListener?.onFailed("加载 Facebook Insert 失败, 错误码： ${error?.errorCode} - ${error?.errorMessage}")
            }

            override fun onAdLoaded(ad: Ad?) {
                adListener?.onLoaded(this@FacebookInsertAd)
                log("加载 Facebook Insert 成功")
            }

            override fun onLoggingImpression(ad: Ad?) {
                log("Facebook Insert onLoggingImpression()")
            }

        })
        insertAd?.loadAd()
    }

    override fun show() {
        if (insertAd?.isAdLoaded == true) {
            insertAd?.show()
        }
    }

    override fun loadAndShow(
        adPosition: String?,
        container: ViewGroup?,
        adListener: AdChainListener?
    ) {
    }

    override fun destroy() {
        insertAd?.destroy()
    }

    override fun onAdResume() {

    }

    override fun onAdPause() {
    }
}