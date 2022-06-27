package app.allever.android.lib.core.base.dialog

import android.animation.Animator
import android.animation.ObjectAnimator
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.PopupWindow
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.helper.ActivityHelper
import app.allever.android.lib.core.helper.DisplayHelper

abstract class AbstractPopWindow<DB : ViewDataBinding>() {

    protected var mBinding: DB

    private var mPopupWindow: PopupWindow
    private var mParams: Params

    private lateinit var mShowAnim: Animator
    private lateinit var mHideAnim: Animator

    init {
        mParams = getParams()
        mPopupWindow = PopupWindow(mParams.width, mParams.height)
        mBinding =
            DataBindingUtil.inflate(LayoutInflater.from(App.context), mParams.layoutId, null, false)
        mPopupWindow.contentView = mBinding.root
        initView()
        initAnim()
    }

    private fun initAnim() {
        mShowAnim = ObjectAnimator.ofFloat(
            mBinding.root,
            "translationY",
            -DisplayHelper.dip2px(mParams.height).toFloat(),
            0f
        )
        mShowAnim.duration = 300
        mShowAnim.interpolator = LinearInterpolator()
        mShowAnim.addListener {
            it.doOnEnd {

            }
        }


        mHideAnim = ObjectAnimator.ofFloat(
            mBinding.root,
            "translationY",
            0f,
            -mParams.height.toFloat()
        )
        mHideAnim.duration = 300
        mHideAnim.interpolator = LinearInterpolator()
        mHideAnim.addListener(onEnd = {
            mPopupWindow.dismiss()
        })
    }

    fun hide(enableAnim: Boolean = mParams.enableAnim) {
        if (enableAnim) {
            mHideAnim.start()
        } else {
            mPopupWindow.dismiss()
        }
    }

    fun isShowing(): Boolean {
        return mPopupWindow.isShowing
    }

    fun show(marginTop: Int = 0, marginLeft: Int = 0) {
        show(ActivityHelper.getTopActivity()?.window?.decorView, marginTop, marginLeft)
    }

    fun show(anchor: View?, marginTop: Int = 0, marginLeft: Int = 0) {
        if (isShowing()) {
            return
        }
        mPopupWindow.showAtLocation(anchor ?: return, Gravity.TOP, marginLeft, marginTop)
        if (mParams.enableAnim) {
            mShowAnim.start()
        }
    }

    /**
     *
     */
    class Params(val layoutId: Int, val width: Int, val height: Int, val enableAnim: Boolean = true)

    abstract fun getParams(): Params

    abstract fun initView()
}