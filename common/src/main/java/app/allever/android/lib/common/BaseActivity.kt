package app.allever.android.lib.common

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import app.allever.android.lib.common.databinding.ActivityBaseBinding
import app.allever.android.lib.core.helper.ViewHelper
import app.allever.android.lib.core.util.StatusBarCompat
import app.allever.android.lib.mvvm.base.BaseMvvmActivity
import app.allever.android.lib.mvvm.base.BaseViewModel
import app.allever.android.lib.mvvm.base.MvvmConfig
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity<DB : ViewDataBinding, VM : BaseViewModel> :
    BaseMvvmActivity<ActivityBaseBinding, VM>() {
    protected lateinit var binding: DB
    override fun onCreate(savedInstanceState: Bundle?) {

        initStatusBar()

        super.onCreate(savedInstanceState)

        initBinding()

        adaptStatusBar()

        setVisibility(mBinding.topBar, showTopBar())

        init()

        mViewModel.init()

        if (enableEventBus()) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (enableEventBus()) {
            EventBus.getDefault().unregister(this)
        }
    }

    override fun getMvvmConfig() = MvvmConfig(R.layout.activity_base, -1)

    protected fun parentBinding(): ActivityBaseBinding = mBinding

    private fun initStatusBar() {
        //透明状态栏
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        StatusBarCompat.translucentStatusBar(this, true)
        //状态栏颜色
        if (isDarkMode()) {
            StatusBarCompat.cancelLightStatusBar(this)
        } else {
            StatusBarCompat.changeToLightStatusBar(this)
        }
    }

    private fun initBinding() {
        val contentConfig = getContentMvvmConfig()
        binding = DataBindingUtil.inflate(layoutInflater, contentConfig.layoutId, null, false)
        parentBinding().contentContainer.addView(binding.root)
        mBinding.setVariable(contentConfig.bindingVariable, mViewModel)
    }

    private fun adaptStatusBar() {
        if (enableAdaptStatusBar()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                StatusBarCompat.setStatusBarColor(this, Color.parseColor("#000000"))
            } else {
                adaptStatusBarView(mBinding.statusBar)
            }
        }
    }

    abstract fun getContentMvvmConfig(): MvvmConfig

    protected open fun enableAdaptStatusBar(): Boolean = true

    protected open fun showTopBar(): Boolean = true

    protected open fun initTopBar(title: String?, leftClickListener: Runnable? = null) {
        ViewHelper.setVisible(mBinding.tvTitle, !TextUtils.isEmpty(title))
        mBinding.tvTitle.text = title
        val ivBack: View = findViewById(R.id.ivBack)
        ivBack.setOnClickListener {
            finish()
            leftClickListener?.run()
        }
    }

    /**
     * @param statusBarView statusBarView
     */
    protected open fun adaptStatusBarView(statusBarView: View?) {
        ViewHelper.adaptStatusBarView(statusBarView)
    }

    protected open fun adaptStatusBarView(statusBarView: View?, statusBarColor: Int) {
        ViewHelper.adaptStatusBarView(statusBarView, statusBarColor)
    }

    /**
     * true: 黑夜模式，白色字体
     * false：白光模式，黑色字体
     *
     * @return isDarkMode
     */
    protected open fun isDarkMode(): Boolean {
        return false
    }

    protected open fun enableEventBus(): Boolean {
        return false
    }
}