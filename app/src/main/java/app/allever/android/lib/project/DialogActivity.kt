package app.allever.android.lib.project

import app.allever.android.lib.mvvm.base.BaseMvvmActivity
import app.allever.android.lib.mvvm.base.BaseViewModel
import app.allever.android.lib.mvvm.base.MvvmConfig
import app.allever.android.lib.project.databinding.ActivityDialogBinding
import app.allever.android.lib.widget.ripple.RippleHelper

class DialogActivity: BaseMvvmActivity<ActivityDialogBinding, DialogViewModel>() {
    override fun getMvvmConfig() = MvvmConfig(R.layout.activity_dialog, BR.dialogVM)
    override fun init() {
        RippleHelper.addRippleView(mBinding.btnBottomDialog)
        RippleHelper.addRippleView(mBinding.btnCenterDialog)
        RippleHelper.addRippleView(mBinding.btnNotificationPopWindow)
        mBinding.btnBottomDialog.setOnClickListener {
            BottomDialog().show(supportFragmentManager)
        }
        mBinding.btnCenterDialog.setOnClickListener {
            CenterDialog(this).show()
        }
        val firstPopWindow = FirstPopWindow()
        mBinding.btnNotificationPopWindow.setOnClickListener {
            firstPopWindow.show()
        }
    }
}

class DialogViewModel : BaseViewModel() {
    override fun init() {
    }
}