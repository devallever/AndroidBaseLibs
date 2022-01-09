package app.allever.android.lib.widget.helper

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Created by allever on 17-8-5.
 * 单位转换工具类
 */

@SuppressLint("StaticFieldLeak")
object DisplayHelper {

    private lateinit var context: Context

    private fun init(context: Context) {
        this.context = context.applicationContext
    }

    /**
     * 将px转换成dip或dp
     * @param context
     * @param px 像素值
     * @density 转换比例 例如1dp = 3px, density值就是3
     *
     * 320x480      mdpi    1dp = 1px   density = 1
     * 480x800      hdpi    1dp = 1.5px density = 1.5
     * 720x1280     xhdpi   1dp = 2px   density = 2
     * 1080x1920    xxhdpi  1dp = 3px   density = 3
     * 1440x2560    xxxhdpi 1dp = 4px   density = 4
     *
     * @result 返回dip或dp值
     */
    fun px2dip(px: Int): Int {
        val density = context.resources.displayMetrics.density
        return (px / density + 0.5f).toInt()
    }

    fun dip2px(dip: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dip * density + 0.5f).toInt()
    }

    fun dip2px(dip: Float): Int {
        val density = context.resources.displayMetrics.density
        return (dip * density + 0.5f).toInt()
    }

    fun px2sp(px: Int): Int {
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return (px / scaledDensity + 0.5f).toInt()
    }

    fun sp2px(sp: Int): Int {
        val scaledDensity = context.resources.displayMetrics.scaledDensity
        return (sp * scaledDensity + 0.5f).toInt()
    }

    fun getDeviceDensity(): Float {
        return context.resources.displayMetrics.density
    }

    fun getDisplayMetrics(): DisplayMetrics {
        return context.resources.displayMetrics
    }

    fun getScreenWidth(): Int {
        return getDisplayMetrics().widthPixels
    }

    fun getScreenHeight(): Int {
        return getDisplayMetrics().heightPixels
    }

    private fun getScreenRealMetrics(): DisplayMetrics {
        val displayMetrics = DisplayMetrics()
        val wm = context.getSystemService(Service.WINDOW_SERVICE) as? WindowManager
        wm?.defaultDisplay?.getRealMetrics(displayMetrics)
        return displayMetrics
    }
}
