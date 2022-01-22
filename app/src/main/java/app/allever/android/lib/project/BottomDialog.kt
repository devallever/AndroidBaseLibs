package app.allever.android.lib.project

import android.content.Context
import app.allever.android.lib.core.base.AbstractBottomDialog

class BottomDialog(context: Context) : AbstractBottomDialog(context) {
    override fun getLayoutId(): Int = R.layout.dialog_bottom
    override fun initView() {
    }
}