package app.allever.android.lib.project

import android.content.Context
import android.view.View
import app.allever.android.lib.core.base.AbstractPopupWindow
import app.allever.android.lib.core.helper.DisplayHelper

class NotificationWindow(context: Context) : AbstractPopupWindow(context) {

    private lateinit var mRootView: View

    init {
        initView()
    }

    override fun getWidth() = DisplayHelper.getScreenWidth() - DisplayHelper.dip2px(20)

    override fun getHeight() = DisplayHelper.dip2px(88)

    private fun initView() {
        //在显示之前，设置背景色+ 阴影
        setContentView(R.layout.dialog_first)
        mPopupWindow.elevation = DisplayHelper.dip2px(20).toFloat()
        mRootView = mPopupWindow.contentView.findViewById(R.id.rootView)

        mRootView.setOnClickListener {
            hide()
        }
    }

    override fun onSingleTapUp() {
        super.onSingleTapUp()
        mPopupWindow.dismiss()
    }
}