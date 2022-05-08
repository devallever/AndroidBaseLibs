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

    /***
     * Activity#onCreate()后调用
     * Fragment#onCreateView()后调用
     */
    abstract fun init()

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {

        }
        mainCoroutine.cancel()
        threadCoroutine.cancel()
        mJob.cancel()
    }
}