package app.allever.android.lib.core.function.datastore

import android.os.Parcelable

interface IDataStore {

    fun putInt(key: String, value: Int)
    fun getInt(key: String): Int

    fun putFloat(key: String, value: Float)
    fun getFloat(key: String): Float

    fun putDouble(key: String, value: Double)
    fun getDouble(key: String): Double

    fun putString(key: String, value: String)
    fun getString(key: String): String

    fun putBoolean(key: String, value: Boolean)
    fun getBoolean(key: String): Boolean

    fun putParcelable(key: String, value: Parcelable)
    fun <T : Parcelable> getParcelable(key: String, clz: Class<T>): T?

}