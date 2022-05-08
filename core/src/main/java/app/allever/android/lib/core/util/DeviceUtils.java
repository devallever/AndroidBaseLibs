package app.allever.android.lib.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import app.allever.android.lib.core.helper.ActivityHelper;

public class DeviceUtils {
    private DeviceUtils(){}

    /***
     * 是否存在NavigationBar
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        if (context == null){
            return false;
        }

        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasNavigationBar;
    }

    public static int getNavigationBarHeight(Context context) {
        if (context == null){
            return 0;
        }
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public String getDeviceId() {
        Activity current = ActivityHelper.INSTANCE.getTopActivity();
        StringBuffer buffer = new StringBuffer();
        buffer.append(android.os.Build.MODEL);
        buffer.append("_");
        buffer.append(android.os.Build.DEVICE);
        buffer.append("_");
        buffer.append(getMacAddress(current).replaceAll(":", ""));
        return buffer.toString();
    }


    /**
     * 获取MAC地址
     *
     * @param context
     * @return
     */
    public String getMacAddress(Context context) {
        String mac = "02:00:00:00:00:00";
        if (context == null) {
            return mac;
        }
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                WifiManager wifi = (WifiManager) context.getApplicationContext()
                        .getSystemService(Context.WIFI_SERVICE);
                if (null != wifi) {
                    WifiInfo info = wifi.getConnectionInfo();
                    mac = info.getMacAddress();
                    if (!TextUtils.isEmpty(mac)) {
                        return mac.toUpperCase(Locale.ENGLISH);
                    }
                }
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                mac = new BufferedReader(new FileReader(new File("/sys/class/net/wlan0/address")))
                        .readLine();
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
                for (NetworkInterface nif : all) {
                    if (!nif.getName().equalsIgnoreCase("wlan0")) {
                        continue;
                    }
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return mac;
                    }
                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:", b));
                    }
                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mac;
    }
}
