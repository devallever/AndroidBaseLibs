//package app.allever.android.lib.ad.admob
//
//import android.view.ViewGroup
//import app.allever.android.lib.ad.admob.AdMobHelper
//import com.allever.lib.ad.AdListener
//import com.allever.lib.ad.BaseAd
//import com.allever.lib.common.app.App
//import com.allever.lib.common.util.ActivityCollector
//import com.allever.lib.common.util.log
//import com.google.android.gms.ads.rewarded.RewardItem
//import com.google.android.gms.ads.rewarded.RewardedAd
//import com.google.android.gms.ads.rewarded.RewardedAdCallback
//import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
//
//@Deprecated("")
//class AdMobVideo: BaseAd() {
//
//    private var rewardedAd: RewardedAd? = null
//    private var mAdListener: AdListener? = null
//
//    override fun load(adPosition: String, container: ViewGroup?, adListener: AdListener?) {
//        rewardedAd = RewardedAd(App.context, adPosition)
//        rewardedAd?.loadAd(
//            AdMobHelper.createAdRequest(),
//            object : RewardedAdLoadCallback() {
//                override fun onRewardedAdLoaded() {
//                    log("AdMob 激励视频 加载成功")
//                    adListener?.onLoaded()
//                }
//
//                override fun onRewardedAdFailedToLoad(errorCode: Int) {
//                    log("AdMob 激励视频 加载失败： $errorCode")
//                    adListener?.onFailed()
//                }
//            })
//        mAdListener = adListener
//    }
//
//    override fun show() {
//        val adCallback = object : RewardedAdCallback() {
//            override fun onRewardedAdOpened() {
//                // Ad opened.
//                log("AdMob 激励视频 Ad onRewardedAdOpened. ")
//                mAdListener?.onShowed()
//            }
//
//            override fun onRewardedAdClosed() {
//                // Ad closed.
//                log("AdMob 激励视频 Ad closed. ")
//                mAdListener?.onDismiss()
//            }
//
//            override fun onUserEarnedReward(rewardItem: RewardItem) {
//                // User earned reward.
//                log("AdMob 激励视频 onUserEarnedReward ")
//            }
//
//            override fun onRewardedAdFailedToShow(errorCode: Int) {
//                // Ad failed to display
//                log("AdMob 激励视频 onRewardedAdFailedToShow $errorCode")
//            }
//        }
//        rewardedAd?.show(ActivityCollector.getTopActivity(), adCallback)
//    }
//
//    override fun loadAndShow(adPosition: String, container: ViewGroup?, adListener: AdListener?) {
//    }
//
//    override fun destroy() {
//    }
//}