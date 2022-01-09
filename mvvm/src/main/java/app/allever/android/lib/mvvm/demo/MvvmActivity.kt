package app.allever.android.lib.mvvm.demo

import app.allever.android.lib.mvvm.BR
import app.allever.android.lib.mvvm.R
import app.allever.android.lib.mvvm.base.BaseMvvmActivity
import app.allever.android.lib.mvvm.databinding.ActivityMvvmBinding

class MvvmActivity : BaseMvvmActivity<ActivityMvvmBinding, MainViewModel>() {
    override fun getLayoutId() = R.layout.activity_mvvm
    override fun getBindingVariable() = BR.mainViewModel
    override fun init() {
        mViewModel.login()
    }
}