package app.allever.android.lib.core.function.notchcompat

import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.helper.BarHelper
import app.allever.android.lib.core.helper.DisplayHelper

object NotchCompat {

    fun adaptNotch(window: Window) {
        //使用刘海区域
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val layoutParams = window.attributes
            layoutParams.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = layoutParams
        } else {
            if (RomUtils.isHuawei() && HuaWeiNotch.hasNotch(window.context)) {
                HuaWeiNotch.displayInNotch(window)
            }

            if (RomUtils.isXiaomi() && XiaoMiNotch.hasNotch(window.context)) {
                XiaoMiNotch.displayInNotch(window)
            }

            //其他系统则可能通过系统设置
        }
    }


    fun adaptNotchWithImmersive(window: Window) {
        BarHelper.setStatusBarTransparent(window)
        adaptNotch(window)
    }

    fun adaptNotchWithFullScreen(window: Window) {
        DisplayHelper.setFullScreen(window)
        adaptNotch(window)
    }

    fun hasNotch(window: Window, task: Runnable? = null) {
        val isNotch = "刘海屏"
        val noNotch = "不是刘海屏"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val decorView: View = window.decorView
            decorView.post {
                val windowInsets: WindowInsets = decorView.rootWindowInsets
                // 当全屏顶部显示黑边时，getDisplayCutout()返回为null
                val displayCutout = windowInsets.displayCutout
                // 获得刘海区域
                val rectList: List<Rect>? = displayCutout?.boundingRects
                if (rectList == null || rectList.isEmpty()) {
                    log(noNotch)
                } else {
                    log(isNotch)
                    task?.run()
                }
            }
        } else {
            if (HuaWeiNotch.hasNotch(window.context) ||
                XiaoMiNotch.hasNotch(window.context) ||
                ViVoNotch.hasNotch(window.context) ||
                OppoNotch.hasNotch(window.context) ||
                MeiZuNotch.hasNotch(window.context)
            ) {
                log(isNotch)
                task?.run()
            } else {
                log(noNotch)
            }
        }
    }
}