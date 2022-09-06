package app.allever.android.lib.project

import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.function.datastore.DataStore
import app.allever.android.lib.core.function.imagecompress.ImageCompress
import app.allever.android.lib.core.function.imageloader.ImageLoader
import app.allever.android.lib.core.function.mediapicker.MediaPickerHelper
import app.allever.android.lib.core.function.permission.DefaultPermissionEngine
import app.allever.android.lib.core.function.permission.PermissionHelper
import app.allever.android.lib.core.helper.ExecutorHelper
import app.allever.android.lib.imagecompress.luban.LubanCompress
import app.allever.android.lib.imageloader.glide.GlideLoader
import app.allever.android.lib.store.mmkv.MMKVStore
import app.allever.android.lib.widget.Widget
import app.allever.android.lib.widget.mediapicker.MediaPicker

class MyApp : App() {
    override fun init() {
        ExecutorHelper.cacheExecutor.execute {
            DataStore.init(MMKVStore)
            ImageLoader.init(this, GlideLoader, ImageLoader.Builder.create())
            ImageCompress.init(LubanCompress())
            PermissionHelper.init(DefaultPermissionEngine)
            Widget.init(this)
            MediaPickerHelper.init(MediaPicker)
        }
    }
}