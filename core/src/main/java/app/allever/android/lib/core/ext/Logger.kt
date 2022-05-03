package app.allever.android.lib.core.ext

import android.util.Log
import app.allever.android.lib.core.BuildConfig

private const val TAG = "ILogger"

fun log(msg: String?) {
    log(TAG, msg)
}

fun log(tag: String, msg: String?) {
    Log.d(tag, msg ?: "")
}

fun debugD(msg: String?) {
    if (BuildConfig.DEBUG) {
        log(TAG, msg)
    }
}

fun debugD(tag: String, msg: String?) {
    if (BuildConfig.DEBUG) {
        Log.d(tag, msg ?: "")
    }
}

fun loge(msg: String?) {
    loge(TAG, msg)
}

fun loge(tag: String, msg: String?) {
    Log.e(tag, msg ?: "")
}

fun debugE(msg: String?) {
    if (BuildConfig.DEBUG) {
        loge(TAG, msg)
    }
}

fun debugE(tag: String, msg: String?) {
    if (BuildConfig.DEBUG) {
        Log.e(tag, msg ?: "")
    }
}