package app.allever.android.lib.network.demo

import app.allever.android.lib.core.function.network.cache.ResponseCache

internal class BannerResponseCache : ResponseCache<BaseResponse<List<BannerData>>>() {

    override fun cacheKey(): String {
        return "banner"
    }
}