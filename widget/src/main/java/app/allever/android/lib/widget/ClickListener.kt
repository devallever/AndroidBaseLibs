package app.allever.android.lib.widget

import android.view.View
import app.allever.android.lib.core.ext.debugD

/**
 * @author allever
 */
abstract class ClickListener : View.OnClickListener {
    private var lastTime: Long = 0

    /**
     * @param v
     */
    abstract fun click(v: View?)

    override fun onClick(v: View) {
        if (checkTime()) {
            debugD("防止多次点击")
            return
        }
        click(v)
    }

    private fun checkTime(): Boolean {
        var flag = true
        val time = System.currentTimeMillis() - lastTime
        if (time > timeInterval()) {
            flag = false
        }
        lastTime = System.currentTimeMillis()
        return flag
    }

    protected fun timeInterval(): Long {
        return TIME_INTERNAL
    }

    companion object {
        /**
         * 设置时间间隔， 默认1秒
         *
         * @return
         */
        protected const val TIME_INTERNAL: Long = 500L
    }
}