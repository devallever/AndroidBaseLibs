package app.allever.android.lib.core.function.permission

import android.content.Context
import android.widget.TextView
import app.allever.android.lib.core.R
import app.allever.android.lib.core.function.permission.PermissionDialog

class WhyRequestPermissionDialog(
    context: Context,
    title: String? = "获取权限",
    message: String? = "使用更多功能",
    val requestTask: Runnable? = null
) :
    PermissionDialog(context, title, message) {


    override fun initView() {
        super.initView()
        findViewById<TextView>(R.id.tvConfirm).text = "确定"
    }

    override fun onClickConfirm() {
        requestTask?.run()
    }
}