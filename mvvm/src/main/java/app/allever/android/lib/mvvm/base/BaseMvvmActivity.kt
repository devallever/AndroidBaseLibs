package app.allever.android.lib.mvvm.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import app.allever.android.lib.core.base.AbstractActivity
import kotlinx.coroutines.launch
import java.lang.reflect.ParameterizedType

abstract class BaseMvvmActivity<DB : ViewDataBinding, VM : BaseViewModel> : AbstractActivity() {
    protected lateinit var mBinding: DB
    protected lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mvvmConfig = getMvvmConfig()
        mBinding = DataBindingUtil.setContentView(this, mvvmConfig.layoutId)
        mBinding.lifecycleOwner = this
        mViewModel = createVM()
        mBinding.setVariable(mvvmConfig.bindingVariable, mViewModel)

        mViewModel.init()

    }

    /**
     * 判断是否viewModel是否初始化
     */
    fun requestViewMode(): VM? {
        return if (this::mViewModel.isInitialized)
            mViewModel
        else {
            mViewModel = createVM()
            mViewModel
        }
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