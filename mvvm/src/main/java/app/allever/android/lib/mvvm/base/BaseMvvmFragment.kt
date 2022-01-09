package app.allever.android.lib.mvvm.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.allever.android.lib.core.base.AbstractFragment
import java.lang.reflect.ParameterizedType

abstract class BaseMvvmFragment<DB : ViewDataBinding, VM : BaseViewModel> : AbstractFragment() {

    private var _binding: DB? = null
    protected val mBinding: DB
        get() = _binding!!
    protected lateinit var mViewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        _binding?.lifecycleOwner = this
        mViewModel =
            ViewModelProvider(this, object : ViewModelProvider.Factory {
                override fun <VM : ViewModel?> create(modelClass: Class<VM>): VM {
                    return modelClass.getDeclaredConstructor().newInstance()
                }
            }).get((this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VM>)
        _binding?.setVariable(getBindingVariable(), mViewModel)

        mViewModel.init()
        init()
        return _binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mViewModel.destroy()
        _binding = null
    }

    abstract fun getLayoutId(): Int
    abstract fun getBindingVariable(): Int
    abstract fun init()
}