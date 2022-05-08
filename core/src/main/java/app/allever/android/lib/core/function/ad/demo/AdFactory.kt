package app.allever.android.lib.core.function.ad.demo

import app.allever.android.lib.core.function.ad.AdBusiness
import app.allever.android.lib.core.function.ad.chain.IAdBusiness
import app.allever.android.lib.core.function.ad.chain.IAdBusinessFactory

class AdFactory : IAdBusinessFactory {
    override fun getAdBusiness(businessName: String): IAdBusiness? {
        return when (businessName) {
            AdBusiness.A -> {
//                AdMobBusiness
                null
            }
            AdBusiness.MI -> {
//                MiMoBusiness
                null
            }
            else -> {
                null
            }
        }
    }
}