package app.allever.android.lib.mvvm.base

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.allever.android.lib.core.base.AbstractActivity
import java.lang.reflect.ParameterizedType

abstract class BaseMvvmActivity<DB : ViewDataBinding, VM : BaseViewModel> : AbstractActivity() {
    protected lateinit var mBinding: DB
    protected lateinit var mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = DataBindingUtil.setContentView(this, getLayoutId())
        mBinding.lifecycleOwner = this
        mViewModel = createVM()
        mBinding.setVariable(getBindingVariable(), mViewModel)

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
            override fun <VM : ViewModel?> create(modelClass: Class<VM>): VM {
                return modelClass.getDeclaredConstructor().newInstance()
            }
        }).get((this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<VM>)
    }

    override fun onDestroy() {
        super.onDestroy()
        mViewModel.destroy()
    }

    abstract fun getLayoutId(): Int
    abstract fun getBindingVariable(): Int
    abstract fun init()
}