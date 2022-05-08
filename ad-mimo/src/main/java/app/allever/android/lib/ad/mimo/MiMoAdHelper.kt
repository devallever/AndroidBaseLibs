package app.allever.android.lib.ad.mimo

import android.content.Context
import android.view.View
import android.view.ViewGroup
import app.allever.android.lib.ad.mimo.MiMoConstants.XIAO_MI_APP_KEY
import app.allever.android.lib.ad.mimo.MiMoConstants.XIAO_MI_APP_TOKEN
import app.allever.android.lib.ad.mimo.MiMoDownload
import app.allever.android.lib.core.BuildConfig
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.function.ad.AdListener
import app.allever.android.lib.core.function.ad.AdManager
import app.allever.android.lib.core.function.ad.BaseAd
import com.miui.zeus.mimo.sdk.MimoSdk
import com.miui.zeus.mimo.sdk.ad.AdWorkerFactory
import com.miui.zeus.mimo.sdk.ad.IAdWorker
import com.miui.zeus.mimo.sdk.ad.IRewardVideoAdWorker
import com.miui.zeus.mimo.sdk.api.IMimoSdkListener
import com.miui.zeus.mimo.sdk.listener.MimoAdListener
import com.miui.zeus.mimo.sdk.listener.MimoRewardVideoListener
import com.xiaomi.ad.common.pojo.AdType

object MiMoAdHelper: AdManager() {

    override fun init(context: Context, appId: String, appKey: String, appToken: String) {
        // 如果担心sdk自升级会影响开发者自身app的稳定性可以关闭，
        // 但是这也意味着您必须得重新发版才能使用最新版本的sdk, 建议开启自升级
        MimoSdk.setEnableUpdate(false)

        MimoSdk.setDebug(BuildConfig.DEBUG) // 正式上线时候务必关闭debug模式
//        MimoSdk.setStaging(BuildConfig.DEBUG) // 正式上线时候务必关闭stage模式

        // 如需要在本地预置插件,请在assets目录下添加mimo_asset.apk;
        MimoSdk.init(context, appId, XIAO_MI_APP_KEY, XIAO_MI_APP_TOKEN, object :
            IMimoSdkListener {
            override fun onSdkInitSuccess() {
                log("初始化米盟成功")
            }

            override fun onSdkInitFailed() {
                log("初始化米盟失败")
            }
        })
    }

    override fun createBannerAd(): BaseAd? {
        return MiMoBanner()
    }

    override fun createInsertAd(): BaseAd? {
        return MiMoInsert()
    }

    override fun createVideoAd(): BaseAd? {
        return MiMoVideo()
    }

    override fun createDownloadAd(): BaseAd? {
        return MiMoDownload()
    }

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
        adListener: AdListener?
    ): IAdWorker? {
        val adWorker = createAdWorker(adType, container, adListener)
        adWorker?.load(adPosition)
        return adWorker
    }

    fun loadAndShow(
        adType: AdType,
        adPosition: String,
        container: ViewGroup?,
        adListener: AdListener?
    ): IAdWorker? {
        val adWorker = createAdWorker(adType, container, adListener)
        adWorker?.loadAndShow(adPosition)
        return adWorker
    }

    fun loadRewardVideo(adPosition: String, adListener: AdListener?): IRewardVideoAdWorker? {
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
                    adListener?.onFailed()
                }

                override fun onAdDismissed() {
                    log("激励视频 onAdDismissed")
                    adListener?.onDismiss()
                }

                override fun onAdPresent() {
                    log("激励视频 onAdPresent")
                    adListener?.onShowed()
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
                    adListener?.playEnd()
                }

                override fun onStimulateSuccess() {
                    log("激励视频 onStimulateSuccess")
                }

                override fun onAdLoaded(p0: Int) {
                    log("激励视频 onAdLoaded")
                    adListener?.onLoaded()
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
        adListener: AdListener?
    ): IAdWorker? {
        var worker: IAdWorker? = null
        try {
            worker = AdWorkerFactory.getAdWorker(App.context, container, object : MimoAdListener {
                override fun onAdLoaded(p0: Int) {
                    log("onAdLoaded")
                    adListener?.onLoaded()
                    container?.visibility = View.VISIBLE
                }

                override fun onAdPresent() {
                    log("onAdPresent")
                    adListener?.onShowed()
                    container?.visibility = View.VISIBLE
                }

                override fun onAdFailed(msg: String?) {
                    log("onAdFailed $msg")
                    adListener?.onFailed()
                }

                override fun onAdDismissed() {
                    log("onAdDismissed")
                    adListener?.onDismiss()
                }

                override fun onAdClick() {
                    log("onAdClick")
                    adListener?.onClick()
                }

                override fun onStimulateSuccess() {
                    log("onStimulateSuccess")
                    adListener?.onStimulateSuccess()
                }
            }, adType)
        } catch (e: Exception) {
            e.printStackTrace()
            adListener?.onFailed()
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
