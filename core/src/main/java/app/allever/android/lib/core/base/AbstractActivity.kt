package app.allever.android.lib.core.base

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import app.allever.android.lib.core.R
import app.allever.android.lib.core.helper.ActivityHelper
import app.allever.android.lib.core.helper.CoroutineHelper
import app.allever.android.lib.core.helper.HandlerHelper
import app.allever.android.lib.core.log
import app.allever.android.lib.core.toast
import java.lang.ref.WeakReference

abstract class AbstractActivity : AppCompatActivity() {

    protected val mHandler by lazy {
        HandlerHelper.mainHandler
    }

    private var mWeakRefActivity: WeakReference<Activity>? = null

    protected val mainCoroutine by lazy {
        CoroutineHelper.mainCoroutine
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log(this.javaClass.simpleName)
        mWeakRefActivity = WeakReference(this)
        ActivityHelper.add(mWeakRefActivity)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        ActivityHelper.remove(mWeakRefActivity)
        super.onDestroy()
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
}