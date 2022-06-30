package app.allever.android.lib.core.base

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import androidx.core.animation.addListener
import androidx.core.animation.doOnEnd
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.helper.DisplayHelper
import razerdp.basepopup.BasePopupFlag
import razerdp.basepopup.BasePopupWindow

abstract class AbstractPopWindow<DB : ViewDataBinding>(context: Context) :
    BasePopupWindow(context) {

    protected var mBinding: DB

    private var mParams: Params

    private lateinit var mShowAnim: Animator
    private lateinit var mHideAnim: Animator

    init {
        mParams = getParams()
        mBinding =
            DataBindingUtil.inflate(LayoutInflater.from(App.context), mParams.layoutId, null, false)
        contentView = mBinding.root
        //透明蒙层
        setBackgroundColor(Color.TRANSPARENT)
        popupGravity = Gravity.CENTER_HORIZONTAL or Gravity.TOP
        setOutSideDismiss(false)
        //是否允许PopupWindow外部响应事件，默为False
        //如果设置为True，则蒙层的事件会透传到其下方
        isOutSideTouchable = true
        //状态栏覆盖模式
        setOverlayStatusbarMode(BasePopupFlag.OVERLAY_MASK)
        setBackPressEnable(false)
        setFitSize(true)
        width = mParams.width
        height = mParams.height
        offsetY = DisplayHelper.dip2px(0)
        initView()
        initAnim()
    }

    override fun onBackPressed(): Boolean {
        context.onBackPressed()
        return true
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
            dismiss()
        })
    }

    fun hide(enableAnim: Boolean = mParams.enableAnim) {
        if (enableAnim) {
            mHideAnim.start()
        } else {
            dismiss()
        }
    }

    fun show(offsetXDp: Int = 0, offsetYDp: Int = 0) {
        if (isShowing) {
            return
        }
        offsetX = DisplayHelper.dip2px(offsetXDp)
        offsetY = DisplayHelper.dip2px(offsetYDp)

        super.showPopupWindow()
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