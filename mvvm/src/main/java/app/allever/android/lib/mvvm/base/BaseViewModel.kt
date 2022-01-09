package app.allever.android.lib.mvvm.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

abstract class BaseViewModel : ViewModel() {
    protected val mJob by lazy {
        Job()
    }

    protected val mainCoroutine by lazy {
        CoroutineScope(Dispatchers.Main + mJob)
    }

    protected val threadCoroutine by lazy {
        CoroutineScope(Dispatchers.Default + mJob)
    }

    abstract fun init()
    fun destroy() {
        mainCoroutine.cancel()
        threadCoroutine.cancel()
        mJob.cancel()
    }
}