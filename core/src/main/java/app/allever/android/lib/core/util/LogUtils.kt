package app.allever.android.lib.core.util

import android.util.Log

object LogUtils {
    private var TAG: String = "ILogger"

    private fun init(tag: String) {
        TAG = tag
    }

    fun d(msg: String) {
        d(TAG, msg)
    }

    fun d(tag: String, msg: String) {
        Log.d(tag, msg)
    }

    fun e(msg: String) {
        e(TAG, msg)
    }

    fun e(tag: String, msg: String) {
        Log.e(tag, msg)
    }
}