package app.allever.android.lib.mvvm.demo

import app.allever.android.lib.mvvm.BR
import app.allever.android.lib.mvvm.R
import app.allever.android.lib.mvvm.base.BaseMvvmFragment
import app.allever.android.lib.mvvm.base.MvvmConfig
import app.allever.android.lib.mvvm.databinding.FragmentMvvmBinding

class MvvmFragment : BaseMvvmFragment<FragmentMvvmBinding, MainFragmentViewModel>() {

    override fun getMvvmConfig() = MvvmConfig(R.layout.fragment_mvvm, BR.fragmentViewModel)

    override fun init() {
        mViewModel.login()
    }
}