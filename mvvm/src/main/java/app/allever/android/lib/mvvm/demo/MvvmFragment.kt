package app.allever.android.lib.mvvm.demo

import app.allever.android.lib.mvvm.BR
import app.allever.android.lib.mvvm.R
import app.allever.android.lib.mvvm.base.BaseMvvmFragment
import app.allever.android.lib.mvvm.databinding.FragmentMvvmBinding

class MvvmFragment : BaseMvvmFragment<FragmentMvvmBinding, MainFragmentViewModel>() {
    override fun getLayoutId() = R.layout.fragment_mvvm
    override fun getBindingVariable() = BR.fragmentViewModel

    override fun init() {
        mViewModel.login()
    }
}