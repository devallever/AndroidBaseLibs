package app.allever.android.lib.core.ext

import android.widget.Toast
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.getString
import app.allever.android.lib.core.helper.HandlerHelper

private val mToast : Toast by lazy {
    val toast = Toast.makeText(App.context, "", Toast.LENGTH_SHORT)
    toast
}

fun toast(resId: Int) {
    toast(getString(resId))
}

fun toast(msg: String?) {
    msg ?: return
    HandlerHelper.mainHandler.post {
        mToast.setText(msg)
        mToast.show()
    }
}