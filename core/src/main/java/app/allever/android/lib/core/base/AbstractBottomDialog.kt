package app.allever.android.lib.core.base

import android.content.Context
import android.view.Gravity
import app.allever.android.lib.core.R

abstract class AbstractBottomDialog(context: Context) :
    AbstractDialog(context, R.style.Theme_Design_BottomSheetDialog) {
    override fun getGravity() = Gravity.BOTTOM
}