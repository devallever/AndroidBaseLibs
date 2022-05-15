package app.allever.android.lib.core.function.ad

import android.content.Context

@Deprecated("")
abstract class AdManager {

    abstract fun init(context: Context, appId: String, appKey: String = "", appToken: String = "")

    open fun createAd(adType: String): BaseAd? {
        return when (adType) {
            ADType.BANNER -> {
                return createBannerAd()
            }
            ADType.INSERT -> {
                return createInsertAd()
            }
            ADType.VIDEO -> {
                return createVideoAd()
            }
            ADType.DOWNLOAD -> {
                return createDownloadAd()
            }
            ADType.NATIVE -> {
                return null
            }

            else -> null
        }
    }

    abstract fun createBannerAd(): BaseAd?

    abstract fun createInsertAd(): BaseAd?

    abstract fun createVideoAd(): BaseAd?

    abstract fun createDownloadAd(): BaseAd?

    abstract fun destroy(context: Context)

}