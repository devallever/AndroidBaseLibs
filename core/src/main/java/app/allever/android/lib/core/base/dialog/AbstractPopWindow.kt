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

    private var popupWindow: PopupWindow
    protected var mParams: Params

    private lateinit var mShowAnim: Animator
    private lateinit var mHideAnim: Animator

    init {
        mParams = getParams()
        popupWindow = PopupWindow(mParams.width, mParams.height)
        mBinding =
            DataBindingUtil.inflate(LayoutInflater.from(App.context), mParams.layoutId, null, false)
        popupWindow.contentView = mBinding.root
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
            popupWindow.dismiss()
        })
    }

    fun hide() {
        if (mParams.enableAnim) {
            mHideAnim.start()
        } else {
            popupWindow.dismiss()
        }
    }

    fun isShowing(): Boolean {
        return popupWindow.isShowing
    }

    fun show() {
        show(ActivityHelper.getTopActivity()?.window?.decorView)
    }

    fun show(anchor: View?) {
        if (isShowing()) {
            return
        }
        popupWindow.showAtLocation(anchor ?: return, Gravity.TOP, 0, 0)
        mShowAnim.start()
    }

    /**
     *
     */
    class Params(val layoutId: Int, val width: Int, val height: Int, val enableAnim: Boolean = true)

    abstract fun getParams(): Params

    abstract fun initView()
}