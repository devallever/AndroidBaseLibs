package app.allever.android.lib.project

import android.view.LayoutInflater
import app.allever.android.lib.core.base.dialog.AbstractSlideBottomDialog
import app.allever.android.lib.core.helper.DisplayHelper
import app.allever.android.lib.project.databinding.DialogBottomBinding

class BottomDialog : AbstractSlideBottomDialog<DialogBottomBinding>() {
    override fun inflateBinding(): DialogBottomBinding =
        DialogBottomBinding.inflate(LayoutInflater.from(context))

    override fun getDialogHeight() = DisplayHelper.dip2px(350)
    override fun initView() {}
}