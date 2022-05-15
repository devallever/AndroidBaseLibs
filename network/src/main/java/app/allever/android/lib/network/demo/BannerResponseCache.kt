package app.allever.android.lib.network.demo

import app.allever.android.lib.network.cache.ResponseCache

class BannerResponseCache : ResponseCache<BaseResponse<List<BannerData>>>() {

    override fun cacheKey(): String {
        return "banner"
    }
}