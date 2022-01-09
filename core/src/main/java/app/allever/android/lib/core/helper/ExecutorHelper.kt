package app.allever.android.lib.core.helper

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ExecutorHelper {
    val cacheExecutor: ExecutorService by lazy {
        Executors.newCachedThreadPool()
    }

    val singleExecutor: ExecutorService by lazy {
        Executors.newSingleThreadExecutor()
    }
}