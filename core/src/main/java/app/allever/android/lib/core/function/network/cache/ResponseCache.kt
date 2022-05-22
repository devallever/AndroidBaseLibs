package app.allever.android.lib.core.function.network.cache

import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.helper.NetworkHelper
import app.allever.android.lib.core.function.network.cache.HttpCacheManager.getCacheTime
import app.allever.android.lib.core.function.network.cache.HttpCacheManager.getStringCache
import app.allever.android.lib.core.function.network.cache.HttpCacheManager.putCache
import app.allever.android.lib.core.function.network.cache.HttpCacheManager.putCacheTime
import app.allever.android.lib.core.function.network.cache.HttpCacheManager.putStringCache
import app.allever.android.lib.core.function.network.response.NetResponse
import app.allever.android.lib.core.helper.GsonHelper.toJson
import app.allever.android.lib.core.helper.ReflectionHelper
import com.google.gson.Gson

abstract class ResponseCache<T : NetResponse<*>> {

    private fun saveCache(result: T) {
        putStringCache(cacheKey(), toJson(result))
        log("缓存response：${toJson(result)}")
        putCacheTime(cacheTimeKey(), System.currentTimeMillis())
    }

    private fun deleteCache() {
        putCache(cacheKey(), null)
        putStringCache(cacheKey(), "")
    }

    fun cacheResponse(response: NetResponse<*>) {
        saveCache(response as T)
    }

    fun <T> getCache(): T? {
        if (System.currentTimeMillis() - getCacheTime(cacheTimeKey()) >= cacheTime()
            && NetworkHelper.isNetworkAvailable(App.context)
        ) {
            deleteCache()
            return null
        }

        try {
            val cacheString = getStringCache(cacheKey())
            val response = Gson().fromJson(
                cacheString,
                ReflectionHelper.getGenericType(this::class.java, 0)
            ) as? T
            response.let {
                log("获取缓存: $cacheString")
                return response
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 默认缓存5分钟
     *
     * @return
     */
    protected open fun cacheTime(): Long {
        return 1000 * 60 * 5
    }

    private fun cacheTimeKey(): String {
        return cacheKey() + "_cache_time"
    }

    /**
     *
     * @return
     */
    abstract fun cacheKey(): String
}