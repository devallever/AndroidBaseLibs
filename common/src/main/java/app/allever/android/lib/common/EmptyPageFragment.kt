package app.allever.android.lib.common

import app.allever.android.lib.common.databinding.EmptyPageBinding
import app.allever.android.lib.mvvm.base.BaseViewModel

class EmptyPageFragment : BaseFragment<EmptyPageBinding, BaseViewModel>() {
    override fun inflate() = EmptyPageBinding.inflate(layoutInflater)

    override fun init() {
    }
}