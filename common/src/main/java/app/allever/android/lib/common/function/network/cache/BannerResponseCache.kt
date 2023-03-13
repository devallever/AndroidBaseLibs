package app.allever.android.lib.common.function.network.cache

import app.allever.android.lib.common.function.network.reponse.BannerData
import app.allever.android.lib.common.function.network.reponse.BaseResponse
import app.allever.android.lib.core.function.network.cache.ResponseCache

class BannerResponseCache : ResponseCache<BaseResponse<List<BannerData>>>() {

    override fun cacheKey(): String {
        return "banner"
    }
}