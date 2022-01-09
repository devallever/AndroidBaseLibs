package app.allever.android.lib.mvp.demo

import android.view.LayoutInflater
import app.allever.android.lib.mvp.base.BaseMvpActivity
import app.allever.android.lib.mvp.databinding.ActivityMvpBinding

class MvpActivity : BaseMvpActivity<MainView, MainPresenter, ActivityMvpBinding>(), MainView {
    override fun bindView(inflater: LayoutInflater) = ActivityMvpBinding.inflate(inflater)
    override fun getPresenter() = MainPresenter()

    override fun init() {
        mPresenter.login()
    }

    override fun updateUsername(username: String) {
        mBinding.tvUsername.text = username
    }
}