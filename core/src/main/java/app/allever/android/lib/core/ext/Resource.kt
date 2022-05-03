package app.allever.android.lib.core.ext

import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import app.allever.android.lib.core.app.App

object Resource {

}

fun getString(@StringRes resId: Int): String {
    return App.context.resources.getString(resId)
}

fun getColor(@ColorRes resId: Int): Int {
    return ContextCompat.getColor(App.context, resId)
}