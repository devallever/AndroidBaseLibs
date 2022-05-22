package app.allever.android.lib.core.helper

import app.allever.android.lib.core.helper.LogHelper.e
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * 反射工具类
 *
 * @author Administrator
 */
object ReflectionHelper {
    /**
     * 通过反射设置某一个对象的某一个参数的值
     *
     * @param obj
     * @param value
     * @param fieldName
     * @return
     */
    operator fun setValue(obj: Any, value: Any?, fieldName: String?): Boolean {
        try {
            val aClass: Class<*> = obj.javaClass
            val field = aClass.getDeclaredField(fieldName)
            field.isAccessible = true
            field[obj] = value
            return true
        } catch (e: Exception) {
            e("set value error:" + e.message)
        }
        return false
    }

    /**
     * 通过反射获取对象中某一个值
     *
     * @param obj
     * @param fieldName
     * @return
     */
    fun getValue(obj: Any, fieldName: String?): Any? {
        try {
            val aClass: Class<*> = obj.javaClass
            val field = aClass.getDeclaredField(fieldName)
            field.isAccessible = true
            return field[obj]
        } catch (e: Exception) {
            e("get value error:" + e.message)
        }
        return null
    }

    /**
     * 通过反射获取某一个对象类中的第position个泛型的类
     *
     * @param clz
     * @param position
     * @return
     */
    fun getGenericType(clz: Class<*>, position: Int): Type {
        val pt = clz.genericSuperclass as ParameterizedType
        return pt.actualTypeArguments[position]
    }
}