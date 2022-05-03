package app.allever.android.lib.core.ext

import android.os.Build
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import app.allever.android.lib.core.app.App

fun getString(@StringRes resId: Int): String {
    return App.context.resources.getString(resId)
}

fun getColor(@ColorRes resId: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        App.context.resources.getColor(resId, null)
    } else {
        App.context.resources.getColor(resId)
    }
}