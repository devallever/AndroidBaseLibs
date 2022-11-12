package app.allever.android.lib.core.function.notchcompat

import android.content.Context
import app.allever.android.lib.core.ext.log

object ViVoNotch {

    private const val VIVO_NOTCH = 0x00000020 // 是否有刘海
    private const val VIVO_FILLET = 0x00000008 // 是否有圆角


    /**
     * 判断是否有刘海屏
     *
     * @param context
     * @return true：有刘海屏；false：没有刘海屏
     */
    fun hasNotch(context: Context): Boolean {
        var result = false
        try {
            val classLoader = context.classLoader
            val ftFeature = classLoader.loadClass("android.util.FtFeature")
            val method =
                ftFeature.getMethod("isFeatureSupport", Int::class.javaPrimitiveType)
            result = method.invoke(ftFeature, VivoNotchUtils.VIVO_NOTCH) as Boolean
        } catch (e: Exception) {
            log("hasNotchAtVivo Exception")
        }
        return result
    }
}