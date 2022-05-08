package app.allever.android.lib.core.function.ad.chain

import android.content.Context
import android.view.ViewGroup
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import org.json.JSONObject

object AdChainHelper {

    val nameAdBusinessMap = mutableMapOf<String, IAdBusiness>()
    val nameConfigMap = mutableMapOf<String, AdConfig.AdConfigBean>()

    var businessFactory: IAdBusinessFactory? = null

    fun init(context: Context, configData: String, factory: IAdBusinessFactory) {
        businessFactory = factory
        parseAdConfig(configData)
        parseBusiness(configData)
    }

    fun loadAd(adName: String, container: ViewGroup?, adChainListener: AdChainListener?) {
        //根据adName获取广告的类型
        val adConfigBean = nameConfigMap[adName]
        val adEngine = AdEngine(adName, adConfigBean, container)
        adEngine.loadAd(adChainListener)
    }

    private fun parseBusiness(configData: String) {
        try {
            val businessList = JSONObject(configData).getJSONArray("business")
            for (i in 0 until businessList.length()) {
                val businessObj = businessList.getJSONObject(i)
                val name = businessObj.getString("name")
                val appId = businessObj.getString("appId")
                val appKey = businessObj.getString("appKey")
                val token = businessObj.getString("token")
                val businessBean = AdConfig.BusinessBean()
                businessBean.name = name
                businessBean.appId = appId
                businessBean.appKey = appKey
                businessBean.token = token

                val adBusiness = businessFactory?.getAdBusiness(name)
                adBusiness?.init(App.context, appId, appKey, token)
                if (adBusiness != null) {
                    nameAdBusinessMap[name] = adBusiness
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun parseAdConfig(configData: String) {
        try {
            val configList = JSONObject(configData).getJSONArray("adConfig")
            for (i in 0 until configList.length()) {
                val adConfigBeanObj = configList.get(i) as JSONObject

                val adConfigBean = AdConfig.AdConfigBean()
                val adName = adConfigBeanObj.getString("name")
                val adType = adConfigBeanObj.getString("type")
                val adChainListObj = adConfigBeanObj.getJSONArray("chain")
                val adChainList = mutableListOf<AdConfig.AdConfigBean.ChainBean>()
                for (j in 0 until adChainListObj.length()) {
                    val chainBeanObj = adChainListObj.getJSONObject(j)
                    val chainBean = AdConfig.AdConfigBean.ChainBean()
                    val business = chainBeanObj.getString("business")
                    val adPosition = chainBeanObj.getString("adPosition")
                    chainBean.business = business
                    chainBean.adPosition = adPosition
                    adChainList.add(chainBean)
                }
                adConfigBean.name = adName
                adConfigBean.type = adType
                adConfigBean.chain = adChainList

                nameConfigMap[adName] = adConfigBean
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        nameConfigMap.map {
            log("${it.value.name} - ${it.value.type}")
        }

    }
}
