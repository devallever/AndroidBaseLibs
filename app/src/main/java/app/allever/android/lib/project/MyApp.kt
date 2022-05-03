package app.allever.android.lib.project

import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.helper.ExecutorHelper
import app.allever.android.lib.store.core.DataStore
import app.allever.android.lib.store.mmkv.MMKVStore

class MyApp : App() {
    override fun init() {
        ExecutorHelper.cacheExecutor.execute {
            DataStore.init(MMKVStore)
        }
    }
}