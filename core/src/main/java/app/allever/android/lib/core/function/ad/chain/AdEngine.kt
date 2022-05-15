package app.allever.android.lib.core.function.ad.chain

import android.view.ViewGroup
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.function.ad.ADType

class AdEngine(
    private val adName: String,
    adConfigBean: AdConfig.AdConfigBean?,
    private val container: ViewGroup?
) {

    private var mAdType: String? = ""

    //广告链
    private var adChainList = mutableListOf<AdConfig.AdConfigBean.ChainBean>()

    init {
        mAdType = adConfigBean?.type
        adConfigBean?.chain?.let {
            adChainList.addAll(it)
        }
    }

    fun loadAd(adChainListener: AdChainListener?) {
        if (adChainList.size > 0) {
            log("请求 $adName")
            val chainBean = adChainList[0]
            adChainList.removeAt(0)
            val businessName = chainBean.business
            if (businessName == null) {
                adChainListener?.onFailed("广告商为空")
                log("广告商为空")
            }

            //根据business获取具体IAdBusiness
            val adPosition = chainBean.adPosition
            val adBusiness = AdChainHelper.nameAdBusinessMap[businessName]
            if (adBusiness == null) {
                adChainListener?.onFailed("没有该模块广告：$businessName")
                log("adBusiness = null, 没有该模块广告：$businessName")
                if (adChainList.isEmpty()) {
                    adChainListener?.onFailed("广告商为空")
                } else {
                    loadAd(adChainListener)
                }
                return
            }
            val ad = adBusiness?.createAd(mAdType!!)
            val listener = object : AdChainListener {
                override fun onLoaded(ad: IAd?) {
                    adChainListener?.onLoaded(ad)
                    log("加载【$businessName】$mAdType: $adPosition 成功")
                }

                override fun onFailed(msg: String) {
                    log("加载【$businessName】$mAdType: $adPosition 失败")
                    if (adChainList.isEmpty()) {
                        adChainListener?.onFailed(msg)
                    } else {
                        loadAd(adChainListener)
                    }
                }

                override fun onShowed() {
                    log("广告展示【$businessName】 $mAdType: $adPosition")
                    adChainListener?.onShowed()
                }

                override fun onDismiss() {
                    log("广告消失【$businessName】 $mAdType: $adPosition")
                    adChainListener?.onDismiss()
                }
            }

            log("请求【$businessName】$mAdType: $adPosition")
            if (mAdType == ADType.BANNER) {
                ad?.loadAndShow(adPosition!!, container, listener)
            } else {
                ad?.load(adPosition!!, container, listener)
            }
        } else {
            log("广告链调用完了, 没有广告了")
            adChainListener?.onFailed("没有广告了")
        }
    }
}