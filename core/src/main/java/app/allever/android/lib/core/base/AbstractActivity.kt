package app.allever.android.lib.core.base

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import app.allever.android.lib.core.R
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.toast
import app.allever.android.lib.core.helper.ActivityHelper
import app.allever.android.lib.core.helper.CoroutineHelper
import app.allever.android.lib.core.helper.DisplayHelper
import app.allever.android.lib.core.helper.HandlerHelper
import app.allever.android.lib.core.helper.LifecycleHelper
import app.allever.android.lib.core.helper.ViewHelper
import app.allever.android.lib.core.util.StatusBarCompat
import app.allever.android.lib.core.widget.swipebacklayout.BGAKeyboardUtil
import app.allever.android.lib.core.widget.swipebacklayout.BGASwipeBackHelper
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

abstract class AbstractActivity : AppCompatActivity(), BGASwipeBackHelper.Delegate {


    protected lateinit var mSwipeBackHelper: BGASwipeBackHelper

    protected val mHandler by lazy {
        HandlerHelper.mainHandler
    }

    private var mWeakRefActivity: WeakReference<Activity>? = null

    protected val mainCoroutine by lazy {
        CoroutineHelper.MAIN
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 在 super.onCreate(savedInstanceState) 之前调用该方法
        if (enableEnterAnim()){
            initSwipeBackFinish()
        }

        if (isFullScreen()) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            StatusBarCompat.translucentStatusBar(this, true)
        }

        //状态栏颜色
        if (isDarkMode()) {
            StatusBarCompat.cancelLightStatusBar(this)
        } else {
            StatusBarCompat.changeToLightStatusBar(this)
        }

        super.onCreate(savedInstanceState)
        log(this.javaClass.simpleName)
        mWeakRefActivity = WeakReference(this)
        ActivityHelper.add(mWeakRefActivity)


        if (enableEnterAnim()) {
            //打开动画
            overridePendingTransition(R.anim.open_begin, R.anim.open_end)
        }
    }

//    override fun setContentView(view: View?) {
//        val start = System.currentTimeMillis()
//        super.setContentView(view)
//        val end = System.currentTimeMillis()
//        val duration = end - start
//        log("页面加载时长：${this::class.java.simpleName} -> $duration ms")
//    }
//
//    override fun setContentView(layoutResID: Int) {
//        val start = System.currentTimeMillis()
//        super.setContentView(layoutResID)
//        val end = System.currentTimeMillis()
//        val duration = end - start
//        log("页面加载时长：${this::class.java.simpleName} -> $duration ms")
//    }

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private fun initSwipeBackFinish() {
        mSwipeBackHelper = BGASwipeBackHelper(this, this)

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true)
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true)
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true)
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow)
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true)
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true)
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f)
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        ActivityHelper.resumeTop(mWeakRefActivity)
        LifecycleHelper.pullRootOwner(this)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        ActivityHelper.remove(mWeakRefActivity)
        super.onDestroy()
    }

    override fun isSupportSwipeBack(): Boolean {
        return true
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    override fun onSwipeBackLayoutSlide(slideOffset: Float) {}

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    override fun onSwipeBackLayoutCancel() {}

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    override fun onSwipeBackLayoutExecuted() {
//        mSwipeBackHelper.swipeBackward();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            mSwipeBackHelper.swipeBackward()
        } else {
            BGAKeyboardUtil.closeKeyboard(this)
            finish()
            //TODO 动画造成关闭界面闪动
            overridePendingTransition(0, 0)
        }
    }

    override fun onBackPressed() {
        if (!enableExitAnim()) {
            super.onBackPressed()
            return
        }
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding) {
            return
        }
        mSwipeBackHelper.backward()
    }

    private var firstPressedBackTime = 0L
    protected fun checkExit(runnable: Runnable? = null) {
        if (System.currentTimeMillis() - firstPressedBackTime < 2000) {
            runnable?.run()
            super.onBackPressed()
        } else {
            toast(R.string.core_click_again_to_exit)
            firstPressedBackTime = System.currentTimeMillis()
        }
    }

    protected fun setVisibility(view: View, show: Boolean) {
        if (show) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    protected fun postDelay(task: Runnable, delay: Long = 1000) {
        App.mainHandler.postDelayed(task, delay)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    protected open fun enableEnterAnim(): Boolean {
        return true
    }

    protected open fun enableExitAnim(): Boolean {
        return true
    }

    protected open fun showTopBar(): Boolean = true

    /**
     * true: 黑夜模式，白色字体
     * false：白光模式，黑色字体
     *
     * @return isDarkMode
     */
    protected open fun isDarkMode(): Boolean {
        return false
    }

    /**
     * 是否全屏
     */
    protected open fun isFullScreen(): Boolean = true

    /**
     * 适配状态栏
     */
    protected fun adaptStatusBar(view: View) {
        ViewHelper.setMarginTop(view, DisplayHelper.getStatusBarHeight(this))
    }
}