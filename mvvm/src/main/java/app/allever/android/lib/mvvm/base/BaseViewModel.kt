package app.allever.android.lib.mvvm.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

abstract class BaseViewModel : ViewModel() {

    private val mJob by lazy {
        Job()
    }

    protected val mainScope by lazy {
        CoroutineScope(Dispatchers.Main + mJob)
    }

    protected val defaultScope by lazy {
        CoroutineScope(Dispatchers.Default + mJob)
    }

    protected val ioScope by lazy {
        CoroutineScope(Dispatchers.IO + mJob)
    }

    /***
     * Activity#onCreate()后调用
     * Fragment#onCreateView()后调用
     */
    abstract fun init()

    override fun onCleared() {
        super.onCleared()
        mainScope.cancel()
        defaultScope.cancel()
        ioScope.cancel()
        mJob.cancel()
    }
}