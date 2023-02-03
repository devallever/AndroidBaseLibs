package app.allever.android.lib.core.function.datastore

import android.content.Context
import android.os.Parcelable
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import app.allever.android.lib.core.app.App
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "config")

object PreferenceStore: IDataStore {
    private val mDatsStore = App.context.dataStore

    override suspend fun putInt(key: String, value: Int) {
        putValue(intPreferencesKey(key), value)
    }

    override suspend fun getInt(key: String) = getValue(intPreferencesKey(key), 0)

    override suspend fun putFloat(key: String, value: Float) {
        putValue(floatPreferencesKey(key), value)
    }

    override suspend fun getFloat(key: String) = getValue(floatPreferencesKey(key), 0F)

    override suspend fun putDouble(key: String, value: Double) {
        putValue(doublePreferencesKey(key), value)
    }

    override suspend fun getDouble(key: String) = getValue(doublePreferencesKey(key), 0.0)

    override suspend fun putLong(key: String, value: Long) {
        putValue(longPreferencesKey(key), value)
    }

    override suspend fun getLong(key: String) = getValue(longPreferencesKey(key), 0L)

    override suspend fun putBoolean(key: String, value: Boolean) {
        putValue(booleanPreferencesKey(key), value)
    }

    override suspend fun getBoolean(key: String) = getBoolean(key, false)

    override suspend fun getBoolean(key: String, default: Boolean): Boolean {
        return getValue(booleanPreferencesKey(key), default)
    }

    override suspend fun putString(key: String, value: String) {
        putValue(stringPreferencesKey(key), value)
    }

    override suspend fun getString(key: String) = getValue(stringPreferencesKey(key), "")

    private suspend fun <T> getValue(key: Preferences.Key<T>, default: T): T {
        return mDatsStore.data.map {
            it[key] ?: default
        }.first()
    }

    override suspend fun putParcelable(key: String, value: Parcelable) {
    }

    override suspend fun <T : Parcelable> getParcelable(key: String, clz: Class<T>): T? {
        return null
    }

    private suspend fun <T> putValue(key: Preferences.Key<T>, value: T) {
        mDatsStore.edit {
            it[key] = value
        }
    }

    private fun <T> readNonNullData(key: Preferences.Key<T>, defValue: T): T {
        return runBlocking {
            mDatsStore.data.map {
                it[key] ?: defValue
            }.first()
        }
    }

    private fun getPreferenceKey(key: String, value: Any): Preferences.Key<*>? {
        when (value) {
            is Int -> {
                return intPreferencesKey(key)
            }
            is Float -> {
                return floatPreferencesKey(key)
            }
            is Double -> {
                return doublePreferencesKey(key)
            }
            is Long -> {
                return longPreferencesKey(key)
            }
            is Boolean -> {
                return booleanPreferencesKey(key)
            }
            is String -> {
                return stringPreferencesKey(key)
            }
            else -> {
                return null
            }
        }
    }
}