package app.allever.android.lib.core.function.datastore

import android.os.Parcelable

/**
 * 数据存储
 */
object DataStore : IDataStore {
    private var mDataStore: IDataStore = DefaultStore()

    fun init(dataStore: IDataStore) {
        mDataStore = dataStore
    }

    override suspend fun putInt(key: String, value: Int) {
        mDataStore.putInt(key, value)
    }

    override suspend fun getInt(key: String) = mDataStore.getInt(key)


    override suspend fun putLong(key: String, value: Long) {
        mDataStore.putLong(key, value)
    }

    override suspend fun getLong(key: String) = mDataStore.getLong(key)



    override suspend fun putFloat(key: String, value: Float) {
        mDataStore.putFloat(key, value)
    }

    override suspend fun getFloat(key: String) = mDataStore.getFloat(key)

    override suspend fun putDouble(key: String, value: Double) {
        mDataStore.putDouble(key, value)
    }

    override suspend fun getDouble(key: String) = mDataStore.getDouble(key)

    override suspend fun putString(key: String, value: String) {
        mDataStore.putString(key, value)
    }

    override suspend fun getString(key: String) = mDataStore.getString(key)

    override suspend fun putBoolean(key: String, value: Boolean) {
        mDataStore.putBoolean(key, value)
    }

    override suspend fun getBoolean(key: String) = mDataStore.getBoolean(key)

    override suspend fun getBoolean(key: String, default: Boolean) = mDataStore.getBoolean(key, default)

    override suspend fun putParcelable(key: String, value: Parcelable) {
        mDataStore.putParcelable(key, value)
    }

    override suspend fun <T : Parcelable> getParcelable(key: String, clz: Class<T>): T? {
        return mDataStore.getParcelable(key, clz)
    }

}