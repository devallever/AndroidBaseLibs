package app.allever.android.lib.mvp.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import java.lang.ref.Reference
import java.lang.ref.WeakReference

abstract class BasePresenter<V : BaseView> {

    protected lateinit var mViewRef: Reference<V>

    protected val mJob by lazy {
        Job()
    }

    protected val mainCoroutine by lazy {
        CoroutineScope(Dispatchers.Main + mJob)
    }

    protected val threadCoroutine by lazy {
        CoroutineScope(Dispatchers.Default + mJob)
    }

    fun attach(v: V) {
        mViewRef = WeakReference(v)
    }

    fun detach(v: V) {
        mViewRef.clear()
        mJob.cancel()
    }

    fun getView(): V? {
        return mViewRef.get()
    }
}