package app.allever.android.lib.mvvm.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import app.allever.android.lib.core.function.paging.BasePagingSource
import app.allever.android.lib.core.function.paging.PagingHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow

open class BaseViewModel : ViewModel() {

    @Deprecated("使用viewModelScope")
    private val mJob by lazy {
        Job()
    }

    @Deprecated("使用viewModelScope")
    protected val mainScope by lazy {
        CoroutineScope(Dispatchers.Main + mJob)
    }

    @Deprecated("使用viewModelScope")
    protected val defaultScope by lazy {
        CoroutineScope(Dispatchers.Default + mJob)
    }

    @Deprecated("使用viewModelScope")
    protected val ioScope by lazy {
        CoroutineScope(Dispatchers.IO + mJob)
    }

    /***
     * Activity#onCreate()后调用
     * Fragment#onCreateView()后调用
     */
    open fun init() {

    }

    fun <T : Any> getPagingFlowData(pageSource: PagingSource<Int, T>): Flow<PagingData<T>> {
        return PagingHelper.getPager(pageSource).cachedIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        mainScope.cancel()
        defaultScope.cancel()
        ioScope.cancel()
        mJob.cancel()
    }
}