package app.allever.android.lib.common

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.viewbinding.ViewBinding
import app.allever.android.lib.common.databinding.ActivityBaseBinding
import app.allever.android.lib.core.helper.ViewHelper
import app.allever.android.lib.core.util.StatusBarCompat
import app.allever.android.lib.mvvm.base.BaseMvvmActivity
import app.allever.android.lib.mvvm.base.BaseViewModel

abstract class BaseActivity<DB : ViewBinding, VM : BaseViewModel> :
    BaseMvvmActivity<ActivityBaseBinding, VM>() {
    protected lateinit var binding: DB
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = inflateChildBinding()
        super.onCreate(savedInstanceState)
        parentBinding().contentContainer.addView(binding.root)
        if (enableAdaptStatusBar()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                StatusBarCompat.setStatusBarColor(this, Color.parseColor("#000000"))
            } else {
                adaptStatusBarView(mBinding.statusBar)
            }
        }
        setVisibility(mBinding.topBar, showTopBar())
        setVisibility(mBinding.statusBar, showTopBar())
    }

    abstract fun inflateChildBinding(): DB

    override fun inflate() = ActivityBaseBinding.inflate(layoutInflater)

    protected fun parentBinding(): ActivityBaseBinding = mBinding


    protected open fun enableAdaptStatusBar(): Boolean = true

    protected open fun initTopBar(
        title: String?,
        showBackIcon: Boolean = true,
        leftClickListener: Runnable? = null
    ) {
        ViewHelper.setVisible(mBinding.tvTitle, !TextUtils.isEmpty(title))
        ViewHelper.setVisible(mBinding.ivBack, showBackIcon)
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
}