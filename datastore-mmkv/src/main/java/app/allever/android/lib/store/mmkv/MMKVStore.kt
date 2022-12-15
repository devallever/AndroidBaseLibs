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

    override fun putInt(key: String, value: Int) {
        mmkv.encode(key, value)
    }

    override fun getInt(key: String) = mmkv.decodeInt(key)
    override fun putLong(key: String, value: Long){
        mmkv.encode(key, value)
    }

    override fun getLong(key: String) = mmkv.decodeLong(key)

    override fun putFloat(key: String, value: Float) {
        mmkv.encode(key, value)
    }

    override fun getFloat(key: String) = mmkv.decodeFloat(key)

    override fun putDouble(key: String, value: Double) {
        mmkv.encode(key, value)
    }

    override fun getDouble(key: String) = mmkv.decodeDouble(key)

    override fun putString(key: String, value: String) {
        mmkv.encode(key, value)
    }

    override fun getString(key: String) = mmkv.decodeString(key) ?: ""

    override fun putBoolean(key: String, value: Boolean) {
        mmkv.encode(key, value)
    }

    override fun getBoolean(key: String): Boolean {
        return mmkv.decodeBool(key)
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return mmkv.decodeBool(key, default)
    }


    override fun putParcelable(key: String, value: Parcelable) {
        mmkv.encode(key, value)
    }

    override fun <T : Parcelable> getParcelable(key: String, clz: Class<T>): T? {
        return mmkv.decodeParcelable(key, clz)
    }
}