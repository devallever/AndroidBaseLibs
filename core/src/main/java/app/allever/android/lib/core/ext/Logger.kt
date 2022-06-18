package app.allever.android.lib.core.ext

import android.util.Log
import app.allever.android.lib.core.BuildConfig

class Logger

private const val TAG = "ILogger"

@Deprecated("")
fun debugD(msg: String?) {
    if (BuildConfig.DEBUG) {
        log(TAG, msg)
    }
}

fun log(msg: String?) {
    log(TAG, msg)
}

fun log(tag: String, msg: String?) {
    if (BuildConfig.DEBUG) {
        logReleaseD(tag, msg)
    }
}

fun logE(msg: String?) {
    logE(TAG, msg)
}

fun logE(tag: String, msg: String?) {
    if (BuildConfig.DEBUG) {
        logReleaseE(tag, msg)
    }
}

fun logReleaseD(tag: String? = TAG, msg: String?) {
    msg ?: return
    Log.d(tag ?: TAG, msg, null)
}

fun logReleaseE(tag: String? = TAG, msg: String?) {
    msg ?: return
    Log.e(tag ?: TAG, msg, null)
}