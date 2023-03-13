package app.allever.android.lib.common

import app.allever.android.lib.common.databinding.EmptyPageDarkBinding
import app.allever.android.lib.mvvm.base.BaseViewModel

class EmptyPageDarkFragment : BaseFragment<EmptyPageDarkBinding, BaseViewModel>() {
    override fun inflate() = EmptyPageDarkBinding.inflate(layoutInflater)

    override fun init() {
    }
}