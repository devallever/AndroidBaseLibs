package app.allever.android.lib.core.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import app.allever.android.lib.core.app.App
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.NetworkInterface
import java.net.SocketException
import java.net.URL

/**
 * Created by chenxz on 2018/4/21.
 */
object NetworkHelper {

    private const val TIMEOUT = 3000 // TIMEOUT

    private val mTelephonyManager: TelephonyManager by lazy {
        App.context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    private var mMobileDataSwitchListener: WeakReference<MobileDataSwitchListener>? = null

    /**
     * * @see TelephonyManager#DATA_DISCONNECTED
     * * @see TelephonyManager#DATA_CONNECTING
     * * @see TelephonyManager#DATA_CONNECTED
     * * @see TelephonyManager#DATA_SUSPENDED
     */
    private val phoneStateListener: PhoneStateListener = object : PhoneStateListener() {
        override fun onDataConnectionStateChanged(state: Int) {
            super.onDataConnectionStateChanged(state)
            mMobileDataSwitchListener?.get()?.onStateChange(state == TelephonyManager.DATA_CONNECTED)
//            LogUtils.debugLogD("网络变化：state = $state")
        }
    }

    interface MobileDataSwitchListener {
        fun onStateChange(isOpen: Boolean)
    }

    fun observeMobileDataSwitchChanged(listener: MobileDataSwitchListener?) {
        mMobileDataSwitchListener = WeakReference(listener)
        mTelephonyManager.listen(
            phoneStateListener,
            PhoneStateListener.LISTEN_DATA_CONNECTION_STATE
        )
    }

    fun unObserveMobileDataSwitch() {
        mTelephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE)
        mMobileDataSwitchListener?.clear()
        mMobileDataSwitchListener = null
    }

    /**
     * check NetworkAvailable
     *
     * @param context
     * @return
     */
    fun isNetworkAvailable(context: Context): Boolean {
        val manager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = manager.activeNetworkInfo
        return !(null == info || !info.isAvailable)
    }

    /**
     * check NetworkConnected
     *
     * @param context
     * @return
     */
    fun isNetworkConnected(context: Context): Boolean {
        val manager =
            context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = manager.activeNetworkInfo
        return !(null == info || !info.isConnected)
    }

    /**
     * 得到ip地址
     *
     * @return
     */
    fun getLocalIpAddress(): String {
        var ret = ""
        try {
            val en = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                val enumIpAddress = en.nextElement().inetAddresses
                while (enumIpAddress.hasMoreElements()) {
                    val netAddress = enumIpAddress.nextElement()
                    if (!netAddress.isLoopbackAddress) {
                        ret = netAddress.hostAddress.toString()
                    }
                }
            }
        } catch (ex: SocketException) {
            ex.printStackTrace()
        }
        return ret
    }


    /**
     * ping "http://www.baidu.com"
     *
     * @return
     */
    private fun pingNetWork(): Boolean {
        var result = false
        var httpUrl: HttpURLConnection? = null
        try {
            httpUrl = URL("http://www.baidu.com").openConnection() as HttpURLConnection
            httpUrl.connectTimeout = TIMEOUT
            httpUrl.connect()
            result = true
        } catch (e: IOException) {
        } finally {
            if (null != httpUrl) {
                httpUrl.disconnect()
            }
        }
        return result
    }

    /**
     * check is3G
     *
     * @param context
     * @return boolean
     */
    @JvmStatic
    fun is3G(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        return activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_MOBILE
    }

    /**
     * isWifi
     *
     * @param context
     * @return boolean
     */
    @JvmStatic
    fun isWifi(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        return activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_WIFI
    }

    /**
     * is2G
     *
     * @param context
     * @return boolean
     */
    @JvmStatic
    fun is2G(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetInfo = connectivityManager.activeNetworkInfo
        return activeNetInfo != null &&
                (activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_EDGE ||
                        activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_GPRS ||
                        activeNetInfo.subtype == TelephonyManager.NETWORK_TYPE_CDMA)
    }

    /**
     * is wifi on
     */
    fun isWifiEnabled(context: Context): Boolean {
        val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        return wifiManager.isWifiEnabled
    }

    /**
     * 判断MOBILE网络是否可用
     */
    fun isMobile(context: Context?): Boolean {
        if (context != null) {
            //获取手机所有连接管理对象(包括对wi-fi,net等连接的管理)
            val manager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            //获取NetworkInfo对象
            val networkInfo = manager.activeNetworkInfo
            //判断NetworkInfo对象是否为空 并且类型是否为MOBILE
            if (networkInfo != null && networkInfo.type == ConnectivityManager.TYPE_MOBILE)
                return networkInfo.isAvailable
        }
        return false
    }
}