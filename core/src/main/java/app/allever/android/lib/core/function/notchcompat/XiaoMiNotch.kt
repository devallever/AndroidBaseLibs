package app.allever.android.lib.core.function.notchcompat

import android.annotation.SuppressLint
import android.content.Context
import android.view.Window

object XiaoMiNotch {

    /*刘海屏全屏显示FLAG*/
    private const val FLAG_NOTCH_SUPPORT = 0x00000100 // 开启配置
    private const val FLAG_NOTCH_PORTRAIT = 0x00000200 // 竖屏配置
    private const val FLAG_NOTCH_HORIZONTAL = 0x00000400 // 横屏配置


    /**
     * 判断是否有刘海屏
     *
     * @param context
     * @return true：有刘海屏；false：没有刘海屏
     */
    @SuppressLint("PrivateApi")
    fun hasNotch(context: Context): Boolean {
        var result = false
        try {
            val classLoader = context.classLoader
            val systemProperties = classLoader.loadClass("android.os.SystemProperties")
            val method = systemProperties.getMethod(
                "getInt",
                String::class.java,
                Int::class.java
            )
            result = method.invoke(systemProperties, "ro.miui.notch", 0) as Int == 1
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }

    /**
     * 设置应用窗口在刘海屏手机使用刘海区
     *
     * 通过添加窗口FLAG的方式设置页面使用刘海区显示
     *
     * @param window 应用页面window对象
     */
    fun displayInNotch(window: Window?) {
        window ?: return
        // 竖屏绘制到耳朵区
        val flag = FLAG_NOTCH_SUPPORT or FLAG_NOTCH_PORTRAIT
        try {
            val method = Window::class.java.getMethod(
                "addExtraFlags",
                Int::class.java
            )
            method.invoke(window, flag)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}