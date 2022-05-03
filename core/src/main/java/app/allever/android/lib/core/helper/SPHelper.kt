package app.allever.android.lib.core.helper

import android.content.Context
import app.allever.android.lib.core.app.App

/**
 * SharedPreferences 工具类
 */
object SPHelper {

    /** SharedPreferences 文件名 */
    private val SP_FILE_NAME = "default_sp_name"
    /** SharedPreferences 对象 */
    private val sharedPref = App.context.getSharedPreferences(SP_FILE_NAME, Context.MODE_PRIVATE)

    /**
     * 保存字符串
     *
     * @param key   键名
     * @param value 值
     */
    fun putString(key: String, value: String) {
        sharedPref.edit().putString(key, value).apply()
    }

    /**
     * 获取字符串
     *
     * @param key      键名
     * @param defValue 默认值
     *
     * @return 字符串
     */
    fun getString(key: String, defValue: String) = sharedPref.getString(key, defValue)!!

    /**
     * 保存整形
     *
     * @param key   键名
     * @param value 值
     */
    fun putInteger(key: String, value: Int) {
        sharedPref.edit().putInt(key, value).apply()
    }

    /**
     * 获取整形
     *
     * @param key      键名
     * @param defValue 默认值
     *
     * @return 整形
     */
    fun getInteger(key: String, defValue: Int) = sharedPref.getInt(key, defValue)

    /**
     * 保存布尔类型
     *
     * @param key   键名
     * @param value 值
     */
    fun putBoolean(key: String, value: Boolean) {
        sharedPref.edit().putBoolean(key, value).apply()
    }

    /**
     * 获取布尔值
     *
     * @param key      键名
     * @param defValue 默认值
     *
     * @return 布尔值
     */
    fun getBoolean(key: String, defValue: Boolean) = sharedPref.getBoolean(key, defValue)

    /**
     * 保存Int
     *
     * @param key      键名
     * @param defValue 默认值
     *
     * @return Long
     */
    fun putInt(key: String, value: Int) {
        sharedPref.edit().putInt(key, value).apply()
    }

    /**
     * 获取Int
     *
     * @param key      键名
     * @param defValue 默认值
     *
     * @return Long
     */
    fun getInt(key: String, value: Int) = sharedPref.getInt(key, value)

    /**
     * 保存Float
     *
     * @param key      键名
     * @param defValue 默认值
     *
     * @return Long
     */
    fun putFloat(key: String, value: Float) {
        sharedPref.edit().putFloat(key, value).apply()
    }

    /**
     * 获取Float
     *
     * @param key      键名
     * @param defValue 默认值
     *
     * @return Long
     */
    fun getFloat(key: String, value: Float) = sharedPref.getFloat(key, value)

    /**
     * 保存Long
     *
     * @param key      键名
     * @param defValue 默认值
     *
     * @return Long
     */
    fun putLong(key: String, value: Long) {
        sharedPref.edit().putLong(key, value).apply()
    }

    /**
     * 获取Long
     *
     * @param key      键名
     * @param defValue 默认值
     *
     * @return Long
     */
    fun getLong(key: String, value: Long) = sharedPref.getLong(key, value)

    /**
     * 根据 key，从 SharedPref 中移除数据
     *
     * @param key 键名
     */
    fun remove(key: String) {
        sharedPref.edit().remove(key).apply()
    }

    /**
     * 获取 SharedPref 中所有数据集合
     *
     * @return 保存的所有数据集合
     */
    fun getAll(): Map<String, *> = sharedPref.all

}