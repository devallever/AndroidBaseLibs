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

fun loge(msg: String?) {
    msg ?: return
    LogUtils.e(msg)
}

fun loge(tag: String, msg: String) {
    LogUtils.e(tag, msg)
}

fun toast(resId: Int) {
    app.allever.android.lib.core.ext.toast(getString(resId))
}

fun toast(msg: String?) {
    app.allever.android.lib.core.ext.toast(msg)
}

fun getString(@StringRes resId: Int): String {
    return App.context.resources.getString(resId)
}