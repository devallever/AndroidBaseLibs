package app.allever.android.lib.core.helper

import android.view.KeyEvent

object KeyEventHelper {
    fun clickBack() {
        val runtime = Runtime.getRuntime()
        runtime.exec("input keyevent " + KeyEvent.KEYCODE_BACK)
    }
}