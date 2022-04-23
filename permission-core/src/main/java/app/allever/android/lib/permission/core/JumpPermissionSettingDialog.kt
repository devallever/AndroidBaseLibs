package app.allever.android.lib.permission.core

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.KeyEvent
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import app.allever.android.lib.core.helper.DisplayHelper
import app.allever.android.lib.permission.core.databinding.DialogJumpPermissionSettingBinding

class JumpPermissionSettingDialog
    (context: Context, val message: String? = "") : Dialog(context, 0) {

    private lateinit var mBinding: DialogJumpPermissionSettingBinding

    private var keyListener: DialogInterface.OnKeyListener =
        object : DialogInterface.OnKeyListener {
            override fun onKey(dialog: DialogInterface?, keyCode: Int, event: KeyEvent?): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK && event?.repeatCount == 0) {
                    return true
                }
                return false
            }
        }

    init {
        val window = window
        window?.setGravity(getGravity())
        window?.decorView?.setPadding(0, 0, 0, 0)
        val layoutParams = window?.attributes
        layoutParams?.width = DisplayHelper.dip2px(258)
        window?.attributes = layoutParams
        window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
//        val view = LayoutInflater.from(context).inflate(
//            R.layout.dialog_jump_permission_setting,
//            null,
//            false
//        )
        mBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.dialog_jump_permission_setting,
            null,
            false
        )
        setContentView(mBinding.root)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {
        if (!TextUtils.isEmpty(message)) {
            mBinding.tvMessage.text = message
        }

        mBinding.tvConfirm.setOnClickListener {
            PermissionHelper.jumpSetting(context, PermissionHelper.RC_CODE_JUMP_SETTING)
            dismiss()
        }

        mBinding.tvCancel.setOnClickListener {
            dismiss()
        }
    }

    open fun getGravity(): Int {
        return Gravity.CENTER
    }

    override fun setCancelable(cancel: Boolean) {
        super.setCancelable(cancel)
        setCanceledOnTouchOutside(cancel)
        if (cancel) {
            setOnKeyListener(null)
        } else {
            setOnKeyListener(keyListener)
        }
    }

    override fun show() {
        setCancelable(true)
        super.show()
    }

    fun show(cancelAble: Boolean) {
        setCancelable(cancelAble)
        super.show()
    }
}