package app.allever.android.lib.project

import app.allever.android.lib.core.base.dialog.AbstractPopWindow
import app.allever.android.lib.core.helper.DisplayHelper
import app.allever.android.lib.project.databinding.DialogFirstBinding

class FirstPopWindow : AbstractPopWindow<DialogFirstBinding>() {

    override fun getParams() =
        Params(
            R.layout.dialog_first,
            DisplayHelper.getScreenWidth() - DisplayHelper.dip2px(20),
            DisplayHelper.dip2px(100)
        )

    override fun initView() {
        mBinding.rootView.setOnClickListener {
            hide()
        }
    }
}