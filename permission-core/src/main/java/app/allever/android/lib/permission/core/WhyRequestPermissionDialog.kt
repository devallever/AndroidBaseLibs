package app.allever.android.lib.permission.core

import android.content.Context
import android.widget.TextView

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