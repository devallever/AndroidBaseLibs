package app.allever.android.lib.core.helper

import android.app.Service
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import app.allever.android.lib.core.helper.SoftKeyboardHelper
import kotlin.jvm.JvmOverloads
import app.allever.android.lib.core.app.App

/**
 * 键盘相关
 */
object SoftKeyboardHelper {
    fun isShown(context: Context?): Boolean {
        val inputManager = getInputManager(context)
        return inputManager != null && inputManager.isAcceptingText
    }

    private fun getInputManager(context: Context?): InputMethodManager? {
        return if (context == null) {
            null
        } else context.getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    @JvmOverloads
    fun showSoftKeyboard(view: View?, delayedTime: Long = 100, force: Boolean = false) {
        if (view == null) {
            return
        }
        val inputMethodManager = getInputManager(view.context) ?: return
        App.mainHandler.postDelayed(
            {
                view.requestFocus()
                view.isFocusable = true
                inputMethodManager.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN)
            },
            delayedTime
        )
    }

    @JvmOverloads
    fun hideSoftKeyboard(view: View?, delayedTime: Long = 0) {
        if (view == null) {
            return
        }
        val inputMethodManager = getInputManager(view.context) ?: return
        if (delayedTime == 0L) {
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        } else {
            App.mainHandler.postDelayed({
                inputMethodManager.hideSoftInputFromWindow(
                    view.windowToken,
                    0
                )
            }, delayedTime)
        }
    }
}