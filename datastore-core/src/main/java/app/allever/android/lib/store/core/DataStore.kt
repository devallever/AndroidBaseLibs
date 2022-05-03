package app.allever.android.lib.store.core

import android.os.Parcelable

/**
 * 数据存储
 */
object DataStore: IDataStore {
    private var mDataStore: IDataStore = DefaultStore()

    fun init(dataStore: IDataStore) {
        mDataStore = dataStore
    }

    override fun putInt(key: String, value: Int) {
        mDataStore.putInt(key, value)
    }

    override fun getInt(key: String) = mDataStore.getInt(key)

    override fun putFloat(key: String, value: Float) {
        mDataStore.putFloat(key, value)
    }

    override fun getFloat(key: String) = mDataStore.getFloat(key)

    override fun putDouble(key: String, value: Double) {
        mDataStore.putDouble(key, value)
    }

    override fun getDouble(key: String) = mDataStore.getDouble(key)

    override fun putString(key: String, value: String) {
        mDataStore.putString(key, value)
    }

    override fun getString(key: String) = mDataStore.getString(key)

    override fun putBoolean(key: String, value: Boolean) {
        mDataStore.putBoolean(key, value)
    }

    override fun getBoolean(key: String) = mDataStore.getBoolean(key)


    override fun putParcelable(key: String, value: Parcelable) {
        mDataStore.putParcelable(key, value)
    }

    override fun <T : Parcelable> getParcelable(key: String, clz: Class<T>): T? {
        return mDataStore.getParcelable(key, clz)
    }

}