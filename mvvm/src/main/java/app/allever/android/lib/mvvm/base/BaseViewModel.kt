package app.allever.android.lib.mvvm.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

abstract class BaseViewModel : ViewModel() {
    /***
     * Activity#onCreate()后调用
     * Fragment#onCreateView()后调用
     */
    abstract fun init()
}