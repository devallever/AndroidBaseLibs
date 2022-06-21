package app.allever.android.lib.core.helper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

object CoroutineHelper {
    private val mJob by lazy {
        Job()
    }

    val MAIN by lazy {
        CoroutineScope(Dispatchers.Main + mJob)
    }

    val DEFAULT by lazy {
        CoroutineScope(Dispatchers.Default + mJob)
    }

    val IO by lazy {
        CoroutineScope(Dispatchers.IO + mJob)
    }

    fun cancelAll() {
        mJob.cancel()
    }

}