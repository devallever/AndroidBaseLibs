package app.allever.android.lib.core.function.datastore

import android.os.Parcelable

interface IDataStore {

    suspend fun putInt(key: String, value: Int)
    suspend fun getInt(key: String): Int

    suspend fun putLong(key: String, value: Long)
    suspend fun getLong(key: String): Long

    suspend fun putFloat(key: String, value: Float)
    suspend fun getFloat(key: String): Float

    suspend fun putDouble(key: String, value: Double)
    suspend fun getDouble(key: String): Double

    suspend fun putString(key: String, value: String)
    suspend fun getString(key: String): String

    suspend fun putBoolean(key: String, value: Boolean)
    suspend fun getBoolean(key: String): Boolean
    suspend fun getBoolean(key: String, default: Boolean): Boolean

    suspend fun putParcelable(key: String, value: Parcelable)
    suspend fun <T : Parcelable> getParcelable(key: String, clz: Class<T>): T?

}