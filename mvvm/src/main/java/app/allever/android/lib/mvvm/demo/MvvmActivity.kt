package app.allever.android.lib.mvvm.demo

import app.allever.android.lib.mvvm.base.BaseMvvmActivity
import app.allever.android.lib.mvvm.databinding.ActivityMvvmBinding

class MvvmActivity : BaseMvvmActivity<ActivityMvvmBinding, MainViewModel>() {
    override fun inflate() = ActivityMvvmBinding.inflate(layoutInflater)

    override fun init() {
        mViewModel.login()
        mBinding.tvTitle.text = ""
    }
}