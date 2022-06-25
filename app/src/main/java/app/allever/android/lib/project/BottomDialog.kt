package app.allever.android.lib.project

import app.allever.android.lib.core.base.AbstractSlideBottomDialog
import app.allever.android.lib.core.helper.DisplayHelper
import app.allever.android.lib.project.databinding.DialogBottomBinding

class BottomDialog : AbstractSlideBottomDialog<DialogBottomBinding>() {
    override fun getLayoutId(): Int = R.layout.dialog_bottom
    override fun getDialogHeight() = DisplayHelper.dip2px(350)
    override fun initView() {}
}