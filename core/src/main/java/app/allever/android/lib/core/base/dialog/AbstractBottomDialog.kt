package app.allever.android.lib.core.base.dialog

import android.content.Context
import android.view.Gravity
import com.google.android.material.R

abstract class AbstractBottomDialog(context: Context) :
    AbstractDialog(context, R.style.Theme_Design_BottomSheetDialog) {
    override fun getGravity() = Gravity.BOTTOM
}