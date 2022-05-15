package app.allever.android.lib.core.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

/**
 * <pre>
 *     author : Jerry
 *     time   : 2020/05/08
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class WifiUtils {

    /**
     * 获取SSID
     *
     * @return WIFI 的SSID
     */
    public static String getWIFISSID(Context mContext) {
        String ssid = "";
//        String ssid ="unknown id";

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O || Build.VERSION.SDK_INT == Build.VERSION_CODES.P) {

            WifiManager mWifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            assert mWifiManager != null;
            WifiInfo info = mWifiManager.getConnectionInfo();

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                return info.getSSID();
            } else {
                return info.getSSID().replace("\"", "");
            }
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1) {

            ConnectivityManager connManager = (ConnectivityManager) mContext.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            assert connManager != null;
            NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
            if (networkInfo.isConnected()) {
                if (networkInfo.getExtraInfo() != null) {
                    return networkInfo.getExtraInfo().replace("\"", "");
                }
            }
        }
        return ssid;
    }

    //获取手机连接wifi的ssid
    private static String getSSIDByNetWorkId(Context mContext) {
        String ssid = "<unknown>";
        WifiManager mWifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        assert mWifiManager != null;
        WifiInfo info = mWifiManager.getConnectionInfo();
        int networkId = info.getNetworkId();
        @SuppressLint("MissingPermission")
        List<WifiConfiguration> configuredNetworks = mWifiManager.getConfiguredNetworks();
        if (null != configuredNetworks && configuredNetworks.size() > 0) {
            for (int i = 0; i < configuredNetworks.size(); i++) {
                WifiConfiguration wifiConfiguration = configuredNetworks.get(i);
                if (wifiConfiguration.networkId == networkId) {
                    ssid = wifiConfiguration.SSID;
                    break;
                }
            }
        }
        if (ssid.contains("\"")) {
            ssid = ssid.replace("\"", "");
        }
        return ssid;
    }

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     *
     * @param context
     * @return true 表示开启
     */
    public static boolean isOPen(Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
//        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        if (gps || network) {
//        if (gps) {
//            return true;
//        }
//        return false;
    }

    /**
     * 打开gps设置页面
     */
    public static void openGps(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException ex) {
            // The Android SDK doc says that the location settings activity
            // may not be found. In that case show the general settings.

            // General settings activity
            intent.setAction(Settings.ACTION_SETTINGS);
            try {
                activity.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //wifi是否可用
    public static boolean isWifiEnabled(Context mCxt) {
        WifiManager mWifiManager = (WifiManager) mCxt.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED) {
            ConnectivityManager connManager = (ConnectivityManager) mCxt
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo wifiInfo = connManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return wifiInfo.isConnected();
        } else {
            return false;
        }
    }

    /**
     * 获取MAC地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {
        String mac = "02:00:00:00:00:00";
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mac = getMacDefault(context);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mac = getMacAddress();
//            mac = getMacFromFile();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mac = getMacFromHardware();
        }
        return mac;
    }

    /**
     * Android  6.0 之前（不包括6.0）
     * 必须的权限  <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     *
     * @param context
     * @return
     */
    private static String getMacDefault(Context context) {
        String mac = "02:00:00:00:00:00";
        if (context == null) {
            return mac;
        }

        WifiManager wifi = (WifiManager) context.getApplicationContext()
                .getSystemService(Context.WIFI_SERVICE);
        if (wifi == null) {
            return mac;
        }
        WifiInfo info = null;
        try {
            info = wifi.getConnectionInfo();
        } catch (Exception e) {
        }
        if (info == null) {
            return null;
        }
        mac = info.getMacAddress();
        if (!TextUtils.isEmpty(mac)) {
            mac = mac.toUpperCase(Locale.ENGLISH);
        }
        return mac;
    }

    /**
     * Android 6.0（包括） - Android 7.0（不包括）
     *
     * @return
     */
    private static String getMacAddress() {
        String WifiAddress = "02:00:00:00:00:00";
        try {
            WifiAddress = new BufferedReader(new FileReader(new File("/sys/class/net/wlan0/address"))).readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return WifiAddress;
    }


    /**
     * 遍历循环所有的网络接口，找到接口是 wlan0
     * 必须的权限 <uses-permission android:name="android.permission.INTERNET" />
     *
     * @return
     */
    private static String getMacFromHardware() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "02:00:00:00:00:00";
    }


    /**
     * @param context
     * @return int
     * @Title: getNetWorkState
     * @Description: 获取当前网络状态
     */
    public static int getNetWorkState(Context context) {
        final int network_none = -1;// 没有连接网络

        final int network_mobile = 0;// 移动网络

        final int network_wifi = 1;// 无线网络

        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return network_wifi;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return network_mobile;
            }
        } else {
            return network_none;
        }
        return network_none;
    }

    /**
     * @return String
     * @Title: getIpAddress
     * @Description: 获取设备ip地址
     */
    public static String getIpAddress() {
        try {
            for (Enumeration<NetworkInterface> enNetI = NetworkInterface.getNetworkInterfaces(); enNetI
                    .hasMoreElements(); ) {
                NetworkInterface netI = enNetI.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = netI.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (inetAddress instanceof Inet4Address && !inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static byte[] getLocalMacAddress2() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    continue;
                }
                return macBytes;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00};
    }
}
