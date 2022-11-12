package app.allever.android.lib.core.function.notchcompat

import android.content.Context
import android.view.Window
import android.view.WindowManager
import app.allever.android.lib.core.ext.log

object HuaWeiNotch {

    /*刘海屏全屏显示FLAG*/
    private const val FLAG_NOTCH_SUPPORT = 0x00010000
    private const val DISPLAY_NOTCH_STATUS = "display_notch_status"

    /**
     * 判断是否有刘海屏
     *
     * @return true：有刘海屏；false：没有刘海屏
     */
    fun hasNotch(context: Context): Boolean {
        var ret = false
        try {
            val cl = context.classLoader
            val hwNotchSizeUtil =
                cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = hwNotchSizeUtil.getMethod("hasNotchInScreen")
            ret = get.invoke(hwNotchSizeUtil) as Boolean
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ret
    }

    /**
     * 设置应用窗口在华为刘海屏手机使用刘海区
     *
     * 通过添加窗口FLAG的方式设置页面使用刘海区显示
     *
     * @param window 应用页面window对象
     */
    fun displayInNotch(window: Window?) {
        window ?: return
        val layoutParams = window.attributes
        try {
            val layoutParamsExCls =
                Class.forName("com.huawei.android.view.LayoutParamsEx")
            val constructor =
                layoutParamsExCls.getConstructor(WindowManager.LayoutParams::class.java)
            val layoutParamsExObj = constructor.newInstance(layoutParams)
            val method =
                layoutParamsExCls.getMethod("addHwFlags", Int::class.java)
            method.invoke(layoutParamsExObj, FLAG_NOTCH_SUPPORT)
        } catch (e: Exception) {
            e.printStackTrace()
            log("hw add notch screen flag api error")
        }
    }

    /**
     * 恢复应用不使用刘海区显示
     *
     *
     * 通过去除窗口FLAG的方式设置页面不使用刘海区显示
     *
     * @param window 应用页面window对象
     */
    fun notDisplayInNotch(window: Window?) {
        window ?: return
        val layoutParams = window.attributes
        try {
            val layoutParamsExCls =
                Class.forName("com.huawei.android.view.LayoutParamsEx")
            val constructor =
                layoutParamsExCls.getConstructor(WindowManager.LayoutParams::class.java)
            val layoutParamsExObj = constructor.newInstance(layoutParams)
            val method =
                layoutParamsExCls.getMethod("clearHwFlags", Int::class.java)
            method.invoke(layoutParamsExObj, FLAG_NOTCH_SUPPORT)
        } catch (e: Exception) {
            e.printStackTrace()
            log("hw clear notch screen flag api error")
        }
    }

}