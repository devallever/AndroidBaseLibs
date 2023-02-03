package app.allever.android.lib.core.function.datastore

import android.os.Parcelable
import app.allever.android.lib.core.helper.SPHelper

class DefaultStore : IDataStore {
    override suspend fun putInt(key: String, value: Int) {
        SPHelper.putInt(key, value)
    }

    override suspend fun getInt(key: String): Int = SPHelper.getInt(key, 0)

    override suspend fun putLong(key: String, value: Long) {
        SPHelper.putLong(key, value)
    }

    override suspend fun getLong(key: String): Long {
        return SPHelper.getLong(key, 0L)
    }

    override suspend fun putFloat(key: String, value: Float) {
        SPHelper.putFloat(key, value)
    }

    override suspend fun getFloat(key: String): Float = SPHelper.getFloat(key, 0F)

    override suspend fun putDouble(key: String, value: Double) {
        SPHelper.putFloat(key, value.toFloat())
    }

    override suspend fun getDouble(key: String): Double = SPHelper.getFloat(key, 0F).toDouble()

    override suspend fun putString(key: String, value: String) {
        SPHelper.putString(key, value)
    }

    override suspend fun getString(key: String): String = SPHelper.getString(key, "")

    override suspend fun putBoolean(key: String, value: Boolean) {
        SPHelper.putBoolean(key, value)
    }

    override suspend fun getBoolean(key: String): Boolean = SPHelper.getBoolean(key, false)

    override suspend fun getBoolean(key: String, default: Boolean): Boolean  = SPHelper.getBoolean(key, default)

    override suspend fun putParcelable(key: String, value: Parcelable) {
        //SP不能存对象了也
    }

    override suspend fun <T : Parcelable> getParcelable(key: String, clz: Class<T>): T? {
        //SP也不能取对象
        return null
    }
}