package app.allever.android.lib.project

import android.content.Context
import app.allever.android.lib.core.base.AbstractPopupWindow
import app.allever.android.lib.core.helper.DisplayHelper
import app.allever.android.lib.project.databinding.DialogFirstBinding

class NotificationWindow(context: Context) : AbstractPopupWindow<DialogFirstBinding>(context) {

    override fun getParams() = Params(
        R.layout.dialog_first,
        DisplayHelper.getScreenWidth() - DisplayHelper.dip2px(20),
        DisplayHelper.dip2px(88)
    )

    override fun initView() {
        mBinding.rootView.setOnClickListener {
            hide()
        }
    }
}