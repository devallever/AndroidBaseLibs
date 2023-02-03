package app.allever.android.lib.store.mmkv

import android.os.Parcelable
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.function.datastore.IDataStore
import com.tencent.mmkv.MMKV

object MMKVStore : IDataStore {

    private var mmkv: MMKV

    init {
        MMKV.initialize(App.context)
        mmkv = MMKV.defaultMMKV()
    }

    /**
     * 初始化
     */
    fun init() {

    }

    override suspend fun putInt(key: String, value: Int) {
        mmkv.encode(key, value)
    }

    override suspend fun getInt(key: String) = mmkv.decodeInt(key)
    override suspend fun putLong(key: String, value: Long){
        mmkv.encode(key, value)
    }

    override suspend fun getLong(key: String) = mmkv.decodeLong(key)

    override suspend fun putFloat(key: String, value: Float) {
        mmkv.encode(key, value)
    }

    override suspend fun getFloat(key: String) = mmkv.decodeFloat(key)

    override suspend fun putDouble(key: String, value: Double) {
        mmkv.encode(key, value)
    }

    override suspend fun getDouble(key: String) = mmkv.decodeDouble(key)

    override suspend fun putString(key: String, value: String) {
        mmkv.encode(key, value)
    }

    override suspend fun getString(key: String) = mmkv.decodeString(key) ?: ""

    override suspend fun putBoolean(key: String, value: Boolean) {
        mmkv.encode(key, value)
    }

    override suspend fun getBoolean(key: String): Boolean {
        return mmkv.decodeBool(key)
    }

    override suspend fun getBoolean(key: String, default: Boolean): Boolean {
        return mmkv.decodeBool(key, default)
    }


    override suspend fun putParcelable(key: String, value: Parcelable) {
        mmkv.encode(key, value)
    }

    override suspend fun <T : Parcelable> getParcelable(key: String, clz: Class<T>): T? {
        return mmkv.decodeParcelable(key, clz)
    }
}