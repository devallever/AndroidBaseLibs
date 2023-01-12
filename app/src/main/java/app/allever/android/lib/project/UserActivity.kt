package app.allever.android.lib.project

import app.allever.android.lib.common.BaseActivity
import app.allever.android.lib.mvvm.base.BaseViewModel
import app.allever.android.lib.project.databinding.ActivityUserBinding

class UserActivity : BaseActivity<ActivityUserBinding, UserViewModel>(){

    override fun inflateChildBinding() = ActivityUserBinding.inflate(layoutInflater)

    override fun init() {
        initTopBar("UserInfo")
    }

}

class UserViewModel: BaseViewModel() {
    override fun init() {

    }
}