package app.allever.android.lib.core.ext

import app.allever.android.lib.core.helper.GsonHelper

fun Any.toJson() = GsonHelper.toJson(this)