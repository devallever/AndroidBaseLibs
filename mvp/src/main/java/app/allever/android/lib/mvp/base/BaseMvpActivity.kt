package app.allever.android.lib.mvp.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import app.allever.android.lib.core.base.AbstractActivity

abstract class BaseMvpActivity<V : BaseView, P : BasePresenter<V>, VB : ViewBinding> :
    AbstractActivity() {

    protected lateinit var mBinding: VB
    protected lateinit var mPresenter: P

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = bindView(layoutInflater)
        setContentView(mBinding.root)

        mPresenter = getPresenter()
        mPresenter.attach(this as V)
        init()

    }

    /**
     * @return
     */
    protected abstract fun bindView(inflater: LayoutInflater): VB
    abstract fun getPresenter(): P
    abstract fun init()
}