package app.allever.android.lib.core.function.notchcompat

import android.content.Context

object MeiZuNotch {

    /**
     * 判断是否有刘海屏
     *
     * @param context
     * @return true：有刘海屏；false：没有刘海屏
     */
    fun hasNotch(context: Context?): Boolean {
        var result = false
        try {
            val clazz = Class.forName("flyme.config.FlymeFeature")
            val field = clazz.getDeclaredField("IS_FRINGE_DEVICE")
            result = field[null] as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }
}