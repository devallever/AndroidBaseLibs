package app.allever.android.lib.project

import app.allever.android.lib.mvvm.base.BaseViewModel
import app.allever.android.lib.mvvm.base.MvvmConfig
import app.allever.android.lib.project.databinding.ActivityUserBinding

class UserActivity : BaseActivity<ActivityUserBinding, UserViewModel>(){

    override fun getContentMvvmConfig() = MvvmConfig(R.layout.activity_user, BR.userVM)

    override fun init() {
        initTopBar("UserInfo")
    }

}

class UserViewModel: BaseViewModel() {
    override fun init() {

    }
}