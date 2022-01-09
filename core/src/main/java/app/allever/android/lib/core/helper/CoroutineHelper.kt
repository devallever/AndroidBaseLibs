package app.allever.android.lib.core.helper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

object CoroutineHelper {
    private val mJob by lazy {
        Job()
    }

    val mainCoroutine by lazy {
        CoroutineScope(Dispatchers.Main + mJob)
    }

    val threadCoroutine by lazy {
        CoroutineScope(Dispatchers.Default + mJob)
    }

    fun cancelAll() {
        mJob.cancel()
    }

}