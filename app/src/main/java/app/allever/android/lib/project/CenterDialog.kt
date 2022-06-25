package app.allever.android.lib.project

import android.content.Context
import app.allever.android.lib.core.base.dialog.AbstractCenterDialog

class CenterDialog(context: Context) : AbstractCenterDialog(context) {
    override fun getLayoutId() = R.layout.dialog_center

    override fun initView() {
    }
}