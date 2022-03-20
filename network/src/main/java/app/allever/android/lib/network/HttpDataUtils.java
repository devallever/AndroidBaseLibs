package app.allever.android.lib.network;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import app.allever.android.lib.core.util.LogUtils;

/**
 * 反射工具类
 *
 * @author Administrator
 */
public class HttpDataUtils {

    /**
     * 通过反射设置某一个对象的某一个参数的值
     *
     * @param obj
     * @param value
     * @param fieldName
     * @return
     */
    public static boolean setValue(Object obj, Object value, String fieldName) {
        try {
            Class aClass = obj.getClass();
            Field field = aClass.getDeclaredField(fieldName);
            if (null != field) {
                field.setAccessible(true);
                field.set(obj, value);
            }
            return true;
        } catch (Exception e) {
            LogUtils.INSTANCE.e("set value error:" + e.getMessage());
        }
        return false;
    }

    /**
     * 通过反射获取对象中某一个值
     *
     * @param obj
     * @param fieldName
     * @return
     */
    public static Object getValue(Object obj, String fieldName) {
        try {
            Class aClass = obj.getClass();
            Field field = aClass.getDeclaredField(fieldName);
            if (null != field) {
                field.setAccessible(true);
                return field.get(obj);
            }
        } catch (Exception e) {
            LogUtils.INSTANCE.e("get value error:" + e.getMessage());
        }
        return null;
    }

    /**
     * 通过反射获取某一个对象类中的第position个泛型的类
     *
     * @param clz
     * @param position
     * @return
     */
    public static Type getGenericType(Class clz, int position) {
        ParameterizedType pt = (ParameterizedType) clz.getGenericSuperclass();
        return pt.getActualTypeArguments()[position];
    }

}
