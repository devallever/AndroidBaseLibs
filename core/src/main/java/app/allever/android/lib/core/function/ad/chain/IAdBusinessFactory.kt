package app.allever.android.lib.core.function.ad.chain

import app.allever.android.lib.core.function.ad.chain.IAdBusiness

interface IAdBusinessFactory {

    fun getAdBusiness(businessName: String): IAdBusiness?

}