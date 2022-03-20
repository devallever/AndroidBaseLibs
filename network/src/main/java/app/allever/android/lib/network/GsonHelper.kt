package app.allever.android.lib.network

import com.google.gson.Gson

object GsonHelper {
    private val mGson = Gson()
    fun toJson(obj: Any?): String {
        return try {
            mGson.toJson(obj)
        } catch (e: Exception) {
            e.message ?: ""
        }
    }

    fun <T> fromJson(json: String, clazz: Class<T>): T? {
        return mGson.fromJson(json, clazz)
    }

    fun getGson(): Gson {
        return mGson
    }
}