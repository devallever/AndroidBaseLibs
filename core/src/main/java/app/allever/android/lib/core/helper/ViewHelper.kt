package app.allever.android.lib.core.helper

import android.graphics.Color
import android.graphics.Rect
import android.os.Build
import android.view.View
import app.allever.android.lib.core.util.StatusBarCompat

object ViewHelper {

    /**
     *
     */
    fun setVisible(view: View, show: Boolean) {
        if (show) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    /**
     *
     */
    fun adaptStatusBarView(statusBarView: View?) {
        adaptStatusBarView(statusBarView, Color.parseColor("#ffffff"))
    }

    fun adaptStatusBarView(statusBarView: View?, statusBarColor: Int) {
        statusBarView ?: return
        val layoutParams = statusBarView.layoutParams
        layoutParams.height = StatusBarCompat.getStatusBarHeight(statusBarView.context)
        statusBarView.layoutParams = layoutParams
        statusBarView.setBackgroundColor(statusBarColor)
    }

    /**
     * 获取屏幕中位置
     */
    fun getLocation(view: View): IntArray? {
        val location = IntArray(2)
        if (Build.VERSION.SDK_INT >= 24) {
            val rect = Rect()
            view.getGlobalVisibleRect(rect)
            location[0] = rect.left
            location[1] = rect.top
        } else {
            view.getLocationOnScreen(location)
        }
        return location
    }

    fun getGlobalVisibleRect(view: View?): Rect? {
        val rect = Rect()
        view?.getGlobalVisibleRect(rect)
        return rect
    }

    fun isInViewArea(view: View?, x: Float, y: Float): Boolean {
        if (view == null) {
            return false
        }
        val r = Rect()
        view.getGlobalVisibleRect(r)
        return x > r.left && x < r.right && y > r.top && y < r.bottom
    }

    fun isInViewAreaWithMargin(
        view: View?,
        x: Float,
        y: Float,
        marginLeft: Int,
        marginTop: Int,
        marginRight: Int,
        marginBottom: Int
    ): Boolean {
        if (view == null) {
            return false
        }
        val r = Rect()
        view.getGlobalVisibleRect(r)
        return x > r.left - marginLeft && x < r.right + marginRight && y > r.top - marginTop && y < r.bottom + marginBottom
    }
}

fun View.isVisible() = visibility == View.VISIBLE

fun View.visible(show: Boolean) {
    ViewHelper.setVisible(this, show)
}