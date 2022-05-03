package app.allever.android.lib.core.function.datastore

import android.os.Parcelable
import app.allever.android.lib.core.helper.SPHelper

class DefaultStore : IDataStore {
    override fun putInt(key: String, value: Int) {
        SPHelper.putInt(key, value)
    }

    override fun getInt(key: String): Int = SPHelper.getInt(key, 0)

    override fun putFloat(key: String, value: Float) {
        SPHelper.putFloat(key, value)
    }

    override fun getFloat(key: String): Float = SPHelper.getFloat(key, 0F)

    override fun putDouble(key: String, value: Double) {
        SPHelper.putFloat(key, value.toFloat())
    }

    override fun getDouble(key: String): Double = SPHelper.getFloat(key, 0F).toDouble()

    override fun putString(key: String, value: String) {
        SPHelper.putString(key, value)
    }

    override fun getString(key: String): String = SPHelper.getString(key, "")

    override fun putBoolean(key: String, value: Boolean) {
        SPHelper.putBoolean(key, value)
    }

    override fun getBoolean(key: String): Boolean = SPHelper.getBoolean(key, false)

    override fun putParcelable(key: String, value: Parcelable) {
        //SP不能存对象了也
    }

    override fun <T : Parcelable> getParcelable(key: String, clz: Class<T>): T? {
        //SP也不能取对象
        return null
    }
}