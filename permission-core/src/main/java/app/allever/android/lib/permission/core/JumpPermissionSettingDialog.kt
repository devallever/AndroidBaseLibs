package app.allever.android.lib.permission.core

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.widget.TextView
import app.allever.android.lib.core.base.AbstractCenterDialog

class JumpPermissionSettingDialog
    (context: Context, val message: String? = "") : AbstractCenterDialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    override fun getLayoutId() = R.layout.dialog_jump_permission_setting

    override fun initView() {
        if (!TextUtils.isEmpty(message)) {
            findViewById<TextView>(R.id.tvMessage).text = message
        }

        findViewById<TextView>(R.id.tvConfirm).setOnClickListener {
            PermissionHelper.jumpSetting(context, PermissionHelper.RC_CODE_JUMP_SETTING)
            dismiss()
        }

        findViewById<TextView>(R.id.tvCancel).setOnClickListener {
            dismiss()
        }
    }

}