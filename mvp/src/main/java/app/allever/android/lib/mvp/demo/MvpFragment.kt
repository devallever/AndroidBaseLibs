package app.allever.android.lib.mvp.demo

import android.view.LayoutInflater
import app.allever.android.lib.mvp.base.BaseMvpFragment
import app.allever.android.lib.mvp.databinding.FragmentMvpBinding

class MvpFragment : BaseMvpFragment<MainView, MainPresenter, FragmentMvpBinding>(), MainView {
    override fun bindView(layoutInflater: LayoutInflater) =
        FragmentMvpBinding.inflate(layoutInflater)

    override fun getPresenter() = MainPresenter()

    override fun init() {
        mPresenter.login()
    }

    override fun updateUsername(username: String) {
        mBinding.tvUsername.text = username
    }

}