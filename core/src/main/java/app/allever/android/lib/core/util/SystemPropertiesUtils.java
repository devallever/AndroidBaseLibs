package app.allever.android.lib.core.util;

import android.util.Log;

import java.lang.reflect.Method;

public class SystemPropertiesUtils {

    private static final String TAG = SystemPropertiesUtils.class.getName();

    public static String get(String key) {
        String value = null;
        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Class[] args = new Class[]{
                    String.class
            };
            Method method = clazz.getMethod("get", args);
            value = (String) (method.invoke(null, key));
        } catch (Throwable tr) {
            Log.e(TAG, "", tr);
        }
        return value;
    }

    public static String get(String key, String defaultValue) {
        String value = null;
        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Class[] args = new Class[]{
                    String.class, String.class
            };
            Method method = clazz.getMethod("get", args);
            value = (String) (method.invoke(null, key, defaultValue));
        } catch (Throwable tr) {
            Log.e(TAG, "", tr);
        }
        return value;
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        boolean value = defaultValue;
        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Class[] args = new Class[]{
                    String.class, boolean.class
            };
            Method method = clazz.getMethod("getBoolean", args);
            value = (Boolean) (method.invoke(null, key, defaultValue));
        } catch (Throwable tr) {
            Log.e(TAG, "", tr);
        }
        return value;
    }

    public static int getInt(String key, int defaultValue) {
        int value = defaultValue;
        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Class[] args = new Class[]{
                    String.class, int.class
            };
            Method method = clazz.getMethod("getInt", args);
            value = (Integer) (method.invoke(null, key, defaultValue));
        } catch (Throwable tr) {
            Log.e(TAG, "", tr);
        }
        return value;
    }

    public static long getLong(String key, long defaultValue) {
        long value = defaultValue;
        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Class[] args = new Class[]{
                    String.class, long.class
            };
            Method method = clazz.getMethod("getLong", args);
            value = (Long) (method.invoke(null, key, defaultValue));
        } catch (Throwable tr) {
            Log.e(TAG, "", tr);
        }
        return value;
    }

//    public static void debug() {
//        Log.d(TAG, "SystemProperties.get(\"gsm.version.baseband\")=" + get("gsm.version.baseband"));
//        Log.d(TAG,
//                "SystemProperties.get(\"gsm.version.ril-impl\")="
//                        + get("gsm.version.ril-impl", "no found"));
//        Log.d(TAG,
//                "SystemProperties.getBoolean(\"media.aac_51_output_enabled\")="
//                        + getBoolean("media.aac_51_output_enabled", false));
//        Log.d(TAG,
//                "SystemProperties.getInt(\"dalvik.vm.heapconcurrentstart\")="
//                        + getInt("dalvik.vm.heapconcurrentstart", -1));
//        Log.d(TAG,
//                "SystemProperties.getLong(\"dalvik.vm.heapidealfree\")="
//                        + getLong("dalvik.vm.heapidealfree", -1));
//    }
}
