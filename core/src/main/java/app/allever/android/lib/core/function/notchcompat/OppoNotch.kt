package app.allever.android.lib.core.function.notchcompat

import android.content.Context

object OppoNotch {

    /**
     * 判断是否有刘海屏
     *
     * @param context
     * @return true：有刘海屏；false：没有刘海屏
     */
    fun hasNotch(context: Context): Boolean {
        return context.packageManager
            .hasSystemFeature("com.oppo.feature.screen.heteromorphism")
    }
}