package app.allever.android.lib.project

import app.allever.android.lib.mvvm.base.BaseViewModel
import app.allever.android.lib.mvvm.base.MvvmConfig
import app.allever.android.lib.project.databinding.ActivityDialogBinding
import app.allever.android.lib.widget.ripple.RippleHelper

class DialogActivity: BaseActivity<ActivityDialogBinding, DialogViewModel>() {
    override fun getContentMvvmConfig() = MvvmConfig(R.layout.activity_dialog, BR.dialogVM)
    override fun init() {
        initTopBar("弹窗")
        RippleHelper.addRippleView(binding.btnBottomDialog)
        RippleHelper.addRippleView(binding.btnCenterDialog)
        RippleHelper.addRippleView(binding.btnNotificationPopWindow)
        binding.btnBottomDialog.setOnClickListener {
            BottomDialog().show(supportFragmentManager)
        }
        binding.btnCenterDialog.setOnClickListener {
            CenterDialog(this).show()
        }
        val firstPopWindow = FirstPopWindow(this)
        binding.btnNotificationPopWindow.setOnClickListener {
            firstPopWindow.show(offsetYDp = 42)
        }
    }
}

class DialogViewModel : BaseViewModel() {
    override fun init() {
    }
}