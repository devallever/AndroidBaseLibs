package app.allever.android.lib.core

import android.widget.Toast
import androidx.annotation.StringRes
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.helper.HandlerHelper
import app.allever.android.lib.core.util.LogUtils

fun log(msg: String) {
    LogUtils.d(msg)
}

fun log(tag: String, msg: String) {
    LogUtils.d(tag, msg)
}

fun loge(msg: String) {
    LogUtils.e(msg)
}

fun loge(tag: String, msg: String) {
    LogUtils.e(tag, msg)
}

fun toast(resId: Int) {
    toast(getString(resId))
}

fun toast(msg: String) {
    HandlerHelper.mainHandler.post {
        Toast.makeText(App.context, msg, Toast.LENGTH_SHORT).show()
    }
}

fun getString(@StringRes resId: Int): String {
    return App.context.resources.getString(resId)
}