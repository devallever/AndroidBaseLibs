package app.allever.android.lib.mvp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import app.allever.android.lib.core.base.AbstractFragment

abstract class BaseMvpFragment<V : BaseView, P : BasePresenter<V>, VB : ViewBinding> :
    AbstractFragment() {
    private var _binding: VB? = null
    protected val mBinding: VB
        get() = _binding!!
    protected lateinit var mPresenter: P

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = bindView(inflater)
        mPresenter = getPresenter()
        mPresenter.attach(this as V)
        init()
        return _binding?.root
    }

    override fun onDestroyView() {
        mPresenter.detach(this as V)
        super.onDestroyView()
        _binding = null
    }


    protected abstract fun bindView(layoutInflater: LayoutInflater): VB
    protected abstract fun getPresenter(): P
    protected abstract fun init()
}