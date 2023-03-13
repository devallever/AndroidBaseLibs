package app.allever.android.lib.common

import androidx.viewbinding.ViewBinding
import app.allever.android.lib.mvvm.base.BaseMvvmFragment
import app.allever.android.lib.mvvm.base.BaseViewModel

abstract class BaseFragment<DB : ViewBinding, VM : BaseViewModel> : BaseMvvmFragment<DB, VM>() {
}