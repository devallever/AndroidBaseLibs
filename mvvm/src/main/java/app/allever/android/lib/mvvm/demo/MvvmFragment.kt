package app.allever.android.lib.mvvm.demo

import app.allever.android.lib.mvvm.base.BaseMvvmFragment
import app.allever.android.lib.mvvm.databinding.FragmentMvvmBinding

class MvvmFragment : BaseMvvmFragment<FragmentMvvmBinding, MainFragmentViewModel>() {

    override fun inflate() = FragmentMvvmBinding.inflate(layoutInflater)

    override fun init() {
        mViewModel.login()
    }
}