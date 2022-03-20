package app.allever.android.lib.network.cache

import android.os.Parcelable
import app.allever.android.lib.core.app.App
import com.tencent.mmkv.MMKV
import java.lang.reflect.Type

object HttpCacheManager {

    private var mmkv: MMKV

    init {
        MMKV.initialize(App.context, App.context.externalCacheDir?.absolutePath)
        mmkv = MMKV.defaultMMKV()
    }

    fun putStringCache(key: String, value: String) {
        mmkv.encode(key, value)
    }

    fun getStringCache(key: String): String {
        return mmkv.decodeString(key) ?: ""
    }

    fun putCache(key: String, value: Parcelable?) {
        mmkv.encode(key, value)
    }

    fun <T : Parcelable> getCache(key: String, clz: Class<T>): T? {
        return mmkv.decodeParcelable(key, clz)
    }

    fun <T : Parcelable> getCaches(key: String, clz: Class<T>): T? {
        return mmkv.decodeParcelable(key, clz)
    }

    fun putCacheTime(key: String, value: Long) {
        mmkv.encode(key, value)
    }

    fun getCacheTime(key: String) = mmkv.decodeLong(key)

    fun clearCache() {
        mmkv.clearAll()
    }

}