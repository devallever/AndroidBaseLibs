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

    /**
     * 不确定mViewModel是否初始化，请调用requestViewMode
     */
    protected lateinit var mViewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mvvmConfig = getMvvmConfig()
        _binding = DataBindingUtil.inflate(inflater, mvvmConfig.layoutId, container, false)
        _binding?.lifecycleOwner = this
        mViewModel = createVM()
        _binding?.setVariable(mvvmConfig.bindingVariable, mViewModel)

        mViewModel.init()
        init()
        return _binding?.root
    }

    /**
     * 判断是否viewModel是否初始化
     */
    fun requestViewMode(): VM? {
        return if (this::mViewModel.isInitialized)
            mViewModel
        else {
            // 解决 Can't access ViewModels from detached fragment
            if (isDetached || fragmentManager == null) {
                return null
            }
            mViewModel = createVM()
            mViewModel
        }
    }

    /**
     * 该方法可用于判断_binding是否初始化
     */
    fun requestBinding(): DB? {
        return _binding
    }

    private fun createVM(): VM {
        return ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <VM : ViewModel> create(modelClass: Class<VM>): VM {
                return modelClass.getDeclaredConstructor().newInstance()
            }
        })[(this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VM>]
    }

    abstract fun getMvvmConfig(): MvvmConfig
    abstract fun init()
}