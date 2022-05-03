package app.allever.android.lib.core.helper

import android.util.Log
import app.allever.android.lib.core.BuildConfig

object LogHelper {
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

    fun debugD(msg: String) {
        if (BuildConfig.DEBUG) {
            d(msg)
        }
    }

    fun debugD(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            d(tag, msg)
        }
    }

    fun e(msg: String) {
        e(TAG, msg)
    }

    fun e(tag: String, msg: String) {
        Log.e(tag, msg)
    }

    fun debugE(msg: String) {
        if (BuildConfig.DEBUG) {
            e(msg)
        }
    }

    fun debugE(tag: String, msg: String) {
        if (BuildConfig.DEBUG) {
            e(tag, msg)
        }
    }
}