package app.allever.android.lib.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import app.allever.android.lib.core.ext.LoggerKt;

/**
 * 反射工具类
 */
public class ReflectionUtils {

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
            LoggerKt.log("set value error:" + e.getMessage());
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
            LoggerKt.log("get value error:" + e.getMessage());
        }
        return null;
    }

    /**
     * 通过反射获取某一个对象类中的第一个泛型的类
     *
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> Class getGenericCls(Class clz) {
        ParameterizedType pt = (ParameterizedType) clz.getGenericSuperclass();
        return (Class<T>) pt.getActualTypeArguments()[0];
    }

    /**
     * 通过反射获取某一个对象类中的第position个泛型的类
     *
     * @param clz
     * @param position
     * @return
     */
    public static <T> Class getGenericCls(Class clz, int position) {
        ParameterizedType pt = (ParameterizedType) clz.getGenericSuperclass();
        return (Class<T>) pt.getActualTypeArguments()[position];
    }


    public static Object createObject(Class clz, Class[] classParam, Object[] paramValues) {
        //调用构造方法,创建对象的Constructor对象，用他来获取构造方法的信息：即用其调用构造方法创建实例
        try {
            Constructor con = clz.getConstructor(classParam);
            //调用构造方法并创建实例
            return con.newInstance(paramValues);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
