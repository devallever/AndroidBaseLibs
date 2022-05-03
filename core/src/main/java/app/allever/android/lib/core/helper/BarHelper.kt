package app.allever.android.lib.core.helper

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowManager

@SuppressLint("StaticFieldLeak")
object BarHelper {
    private const val STATUS_BAR_HEIGHT = "status_bar_height"
    private const val NAVIGATION_BAR_HEIGHT = "navigation_bar_height"

    private lateinit var context: Context

    private fun init(context: Context) {
        this.context = context.applicationContext
    }

    /**
     * 设置透明状态栏
     */
    fun setStatusBarTransparent(window: Window) {
        // 透明状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = Color.argb(0, 0, 0, 0)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(): Int =
        getXBarHeight(STATUS_BAR_HEIGHT)

    /**
     * 获取导航栏栏高度
     */

    fun getNavigationBarHeight(): Int =
        getXBarHeight(NAVIGATION_BAR_HEIGHT)

    private fun getXBarHeight(parameterName: String): Int {
        var height = 0
        val resourceId: Int =
            context.resources.getIdentifier(parameterName, "dimen", "android")
        if (resourceId > 0) {
            height = context.resources.getDimensionPixelSize(resourceId)
        }

        return height
    }
}