package app.allever.android.lib.core.base

import android.app.Activity
import android.content.Context
import android.view.*
import android.view.animation.TranslateAnimation
import android.widget.PopupWindow
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.helper.DisplayHelper
import java.lang.ref.WeakReference
import kotlin.math.abs

open class AbstractPopupWindow(ctx: Context) : BaseDialog(ctx), IDialog {

    private val mDismissDelay = 300L
    private val mAnimationDuration = 100L
    protected var yOffset = 0

    inner class CustomDetector(view: View) : GestureDetector.SimpleOnGestureListener() {

        val v = view
        var orgX: Int = 0

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            onSingleTapUp()
            resetRunnable()
            return super.onSingleTapUp(e)
        }

        override fun onScroll(e1: MotionEvent?, p1: MotionEvent?, x: Float, y: Float): Boolean {
            resetRunnable()
            return true
        }

        override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
            return super.onSingleTapConfirmed(e)
        }

        override fun onContextClick(e: MotionEvent?): Boolean {
            return super.onContextClick(e)
        }

        override fun onShowPress(e: MotionEvent?) {
            super.onShowPress(e)
        }

        override fun onFling(p0: MotionEvent?, p1: MotionEvent?, p2: Float, p3: Float): Boolean {
            val moveX = p1?.rawX?.toInt() ?: 0
            val moveY = p1?.rawY?.toInt() ?: 0
            val downX = p0?.rawX?.toInt() ?: 0
            val downY = p0?.rawY?.toInt() ?: 0
            if (abs(moveX - downX) < abs(moveY - downY)) {
                if (downY > moveY) {
                    moveToTop()
                }
            }
            return true
        }

        private fun moveToTop() {
            val toY = DisplayHelper.dip2px(getHeight())
            if (ctxRef.get() != null) {
                performTranslate(
                    0F,
                    0F,
                    0F,
                    -toY.toFloat()
                )
            }
        }

        private fun moveToStart() {
            performTranslate(
                0F,
                -DisplayHelper.getScreenWidth().toFloat(),
                0F,
                0F
            )
        }

        override fun onDown(event: MotionEvent?): Boolean {
            orgX = event?.x?.toInt() ?: 0
            App.mainHandler.removeCallbacks(mHideTask)
            return super.onDown(event)
        }

        private fun moveToEnd() {
            performTranslate(
                0F,
                DisplayHelper.getScreenWidth().toFloat(),
                0F,
                0F
            )
        }

        private fun performTranslate(fromX: Float, toX: Float, fromY: Float, toY: Float) {
            removeRunnable()
            performTranslate(
                v,
                fromXDelta = fromX,
                toXDelta = toX,
                fromYDelta = fromY,
                toYDelta = toY,
                mAnimationDuration,
                repeatCount = 0
            )
            App.mainHandler.postDelayed({
                mPopupWindow.dismiss()
            }, mDismissDelay)
        }
    }

    protected val mHideTask = Runnable {
        hide()
    }

    //需要在构建PopupWindow的时候穿进去宽高，否则有黑色底色
    protected var mPopupWindow = PopupWindow(getWidth(), getHeight())
    protected lateinit var anchor: View

    open fun getWidth(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    open fun getHeight(): Int {
        return WindowManager.LayoutParams.WRAP_CONTENT
    }

    protected open fun onSingleTapUp() {
    }

    fun setContentView(resId: Int) {
        mPopupWindow.contentView = LayoutInflater.from(ctxRef.get()).inflate(resId, null)
        val gestureDetector = GestureDetector(ctxRef.get(), CustomDetector(mPopupWindow.contentView))
        mPopupWindow.contentView.setOnTouchListener { _, event -> gestureDetector.onTouchEvent(event) }
    }

    fun setWidth(width: Int) {
        mPopupWindow.width = width
    }

    fun setHeight(height: Int) {
        mPopupWindow.height = height
    }

    fun setAnchorView(anchor: View) {
        this.anchor = anchor
    }

    override fun show() {
        show(anchor)
    }

    open fun show(anchor: View) {
        if (isShowing()) {
            resetRunnable()
        } else {
            showAtLocation(
                anchor,
                Gravity.TOP,
                0,
                0
            )
        }
    }

    open fun showAtLocation(anchor: View, gravity: Int, xOffset: Int, yOffset: Int) {
        this.yOffset = yOffset
        performTranslate(
            mPopupWindow.contentView,
            fromXDelta = 0F,
            toXDelta = 0F,
            -getHeight().toFloat(),
            toYDelta = 0F,
            duration = mDismissDelay,
            repeatCount = 0
        )
        val activity = (anchor.context as? Activity)
        if (activity?.isFinishing == true || activity?.isDestroyed == true) {
            return
        }
        mPopupWindow.showAtLocation(anchor, gravity, xOffset, yOffset)
        resetRunnable()
    }

    private fun performExitAnimation() {
        performTranslate(
            mPopupWindow.contentView,
            fromXDelta = 0F,
            toXDelta = 0F,
            fromYDelta = 0F,
            -getHeight().toFloat(),
            duration = mDismissDelay,
            repeatCount = 0
        )
    }

    protected fun removeRunnable() {
        App.mainHandler.removeCallbacks(mHideTask)
    }

    protected fun resetRunnable() {
        removeRunnable()
        App.mainHandler.postDelayed(mHideTask, 8000)
    }

    override fun isShowing(): Boolean {
        return mPopupWindow.isShowing
    }

    override fun hide() {
        performExitAnimation()
        App.mainHandler.postDelayed({
            mPopupWindow.dismiss()
        }, mDismissDelay)
    }

    fun performTranslate(
        view: View,
        fromXDelta: Float,
        toXDelta: Float,
        fromYDelta: Float,
        toYDelta: Float,
        duration: Long,
        repeatCount: Int
    ) {
        val translateAnimation = TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta)
        translateAnimation.fillAfter = true
        translateAnimation.duration = duration
        translateAnimation.repeatCount = repeatCount
        view.startAnimation(translateAnimation)
    }
}

abstract class BaseDialog(ctx: Context) : IDialog {

    protected var ctxRef: WeakReference<Context> = WeakReference<Context>(ctx)

    override fun show() {

    }

    override fun hide() {

    }

    override fun isShowing(): Boolean {
        return false
    }
}

interface IDialog {

    fun show()
    fun hide()
    fun isShowing(): Boolean
}