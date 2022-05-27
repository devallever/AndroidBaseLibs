package app.allever.android.lib.widget.ext

import android.widget.Toast
import app.allever.android.lib.core.app.App
import app.allever.android.lib.widget.CustomToast

object CustomToast {

}

fun customToast(msg: String?) {
    App.mainHandler.post { CustomToast.builder().text(msg?:"").show() }
}

fun customToastLong(msg: String?) {
    App.mainHandler.post { CustomToast.builder().duration(Toast.LENGTH_LONG).text(msg?:"").show() }
}

fun customToast(onlyDebug: Boolean, msg: String?) {
    App.mainHandler.post { CustomToast.builder().text(msg?:"").onlyDebug(onlyDebug).show() }
}