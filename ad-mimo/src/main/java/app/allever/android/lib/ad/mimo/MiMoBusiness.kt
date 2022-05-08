package app.allever.android.lib.ad.mimo

import android.content.Context
import android.view.View
import android.view.ViewGroup
import app.allever.android.lib.core.BuildConfig
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.function.ad.chain.AdChainListener
import app.allever.android.lib.core.function.ad.chain.IAd
import app.allever.android.lib.core.function.ad.chain.IAdBusiness
import com.miui.zeus.mimo.sdk.MimoSdk
import com.miui.zeus.mimo.sdk.ad.AdWorkerFactory
import com.miui.zeus.mimo.sdk.ad.IAdWorker
import com.miui.zeus.mimo.sdk.ad.IRewardVideoAdWorker
import com.miui.zeus.mimo.sdk.api.IMimoSdkListener
import com.miui.zeus.mimo.sdk.listener.MimoAdListener
import com.miui.zeus.mimo.sdk.listener.MimoRewardVideoListener
import com.xiaomi.ad.common.pojo.AdType

object MiMoBusiness: IAdBusiness() {
    override fun init(context: Context, appId: String, appKey: String, appToken: String) {
        // 如果担心sdk自升级会影响开发者自身app的稳定性可以关闭，
        // 但是这也意味着您必须得重新发版才能使用最新版本的sdk, 建议开启自升级
        MimoSdk.setEnableUpdate(false)

        MimoSdk.setDebug(BuildConfig.DEBUG) // 正式上线时候务必关闭debug模式
//        MimoSdk.setStaging(BuildConfig.DEBUG) // 正式上线时候务必关闭stage模式

        // 如需要在本地预置插件,请在assets目录下添加mimo_asset.apk;
        MimoSdk.init(context, appId,
            MiMoConstants.XIAO_MI_APP_KEY,
            MiMoConstants.XIAO_MI_APP_TOKEN, object :
                IMimoSdkListener {
                override fun onSdkInitSuccess() {
                    log("初始化米盟成功")
                }

                override fun onSdkInitFailed() {
                    log("初始化米盟失败")
                }
            })
    }

    override fun createBannerAd(): IAd? = MiMoBannerAd()

    override fun createInsertAd(): IAd? = MiMoInsertAd()

    override fun createVideoAd(): IAd? = MiMoVideoAd()

    override fun createDownloadAd(): IAd? = null

    override fun createNativeAd(adType: String): IAd? = null

    override fun destroy(context: Context) {

    }

    fun show(adWorker: IAdWorker?) {
        try {
            adWorker?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun load(
        adType: AdType,
        adPosition: String,
        container: ViewGroup?,
        AdChainListener: AdChainListener?
    ): IAdWorker? {
        val adWorker = createAdWorker(adType, container, AdChainListener)
        if (adPosition == "") {
            log("广告位为空")
            return null
        }
        return try {
            adWorker?.load(adPosition)
            adWorker
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun loadAndShow(
        adType: AdType,
        adPosition: String,
        container: ViewGroup?,
        AdChainListener: AdChainListener?
    ): IAdWorker? {
        val adWorker = createAdWorker(adType, container, AdChainListener)
        if (adPosition == "") {
            log("广告位为空")
            return null
        }
        return try {
            adWorker?.loadAndShow(adPosition)
            adWorker
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    }

    fun loadRewardVideo(adPosition: String, AdChainListener: AdChainListener?): IRewardVideoAdWorker? {
        var adWorker: IRewardVideoAdWorker? = null
        try {
            adWorker = AdWorkerFactory
                .getRewardVideoAdWorker(
                    App.context,
                    adPosition,
                    AdType.AD_REWARDED_VIDEO
                )
            adWorker.setListener(object : MimoRewardVideoListener {
                override fun onAdFailed(p0: String?) {
                    log("激励视频 onAdFailed: $p0")
                    AdChainListener?.onFailed("激励视频 onAdFailed: $p0")
                }

                override fun onAdDismissed() {
                    log("激励视频 onAdDismissed")
                    AdChainListener?.onDismiss()
                }

                override fun onAdPresent() {
                    log("激励视频 onAdPresent")
                    AdChainListener?.onShowed()
                }

                override fun onAdClick() {
                    log("激励视频 onAdClick")
                }

                override fun onVideoPause() {
                    log("激励视频 onVideoPause")
                }

                override fun onVideoStart() {
                    log("激励视频 onVideoStart")
                }

                override fun onVideoComplete() {
                    log("激励视频 onVideoComplete")
                    AdChainListener?.playEnd()
                }

                override fun onStimulateSuccess() {
                    log("激励视频 onStimulateSuccess")
                }

                override fun onAdLoaded(p0: Int) {
                    log("激励视频 onAdLoaded")
                    AdChainListener?.onLoaded(null)
                }

            })
            adWorker.load()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return adWorker
    }

    private fun createAdWorker(
        adType: AdType,
        container: ViewGroup?,
        adChainListener: AdChainListener?
    ): IAdWorker? {
        var worker: IAdWorker? = null
        try {
            worker = AdWorkerFactory.getAdWorker(App.context, container, object : MimoAdListener {
                override fun onAdLoaded(p0: Int) {
                    log("onAdLoaded")
                    adChainListener?.onLoaded(null)
                    container?.visibility = View.VISIBLE
                }

                override fun onAdPresent() {
                    log("onAdPresent")
                    adChainListener?.onShowed()
                    container?.visibility = View.VISIBLE
                }

                override fun onAdFailed(msg: String?) {
                    log("onAdFailed $msg")
                    adChainListener?.onFailed("onAdFailed $msg")
                }

                override fun onAdDismissed() {
                    log("onAdDismissed")
                    adChainListener?.onDismiss()
                }

                override fun onAdClick() {
                    log("onAdClick")
                    adChainListener?.onClick()
                }

                override fun onStimulateSuccess() {
                    log("onStimulateSuccess")
                    adChainListener?.onStimulateSuccess()
                }
            }, adType)
        } catch (e: Exception) {
            e.printStackTrace()
            adChainListener?.onFailed("加载失败")
        }
        return worker
    }

    fun showVideo(iRewardVideoAdWorker: IRewardVideoAdWorker?) {
        try {
            iRewardVideoAdWorker?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun destroy(adWorker: IAdWorker?) {
        try {
            adWorker?.recycle()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun destroyVideo(iRewardVideoAdWorker: IRewardVideoAdWorker?) {
        try {
            iRewardVideoAdWorker?.recycle()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}