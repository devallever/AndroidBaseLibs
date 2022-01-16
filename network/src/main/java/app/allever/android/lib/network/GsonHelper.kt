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
}