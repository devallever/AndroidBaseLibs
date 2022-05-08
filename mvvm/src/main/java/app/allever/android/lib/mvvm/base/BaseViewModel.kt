package app.allever.android.lib.mvvm.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

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
        viewModelScope.launch {

        }
        mainCoroutine.cancel()
        threadCoroutine.cancel()
        mJob.cancel()
    }
}