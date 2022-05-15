package app.allever.android.lib.core.helper

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.ActivityManager
import android.app.KeyguardManager
import android.app.Notification
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.os.Process
import android.os.StrictMode
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationManagerCompat
import app.allever.android.lib.core.app.App

object SystemHelper {

    private val TAG = SystemHelper::class.java.simpleName

    /**
     * @return 硬件制造商
     */
    /**
     * 硬件制造商
     */
    val manufacturer = Build.MANUFACTURER
    /**
     * @return 手机品牌
     */
    /**
     * 手机品牌
     */
    val brand = Build.BRAND
    /**
     * @return 设备名
     */
    /**
     * 设备名
     */
    val deviceName = Build.DEVICE
    /**
     * @return 手机型号
     */
    /**
     * 用户可见的手机型号
     */
    val model = Build.MODEL
    /**
     * @return 显示屏参数
     */
    /**
     * 显示屏参数
     */
    val display = Build.DISPLAY
    /**
     * @return 产品名称（手机厂商）
     */
    /**
     * 产品名称（手机厂商）
     */
    val product = Build.PRODUCT
    /**
     * @return 设备唯一识别码
     */
    /**
     * 设备唯一识别码
     */
    val fingerprint = Build.FINGERPRINT

    /**
     * CPU的指令集
     */
    private val CPU_ABI = Build.SUPPORTED_ABIS

    /**
     * 修订版本列表
     */
    private val BUILD_ID = Build.ID

    /**
     * 描述Build的标签???
     */
    private val TAGS = Build.TAGS

    /**
     * @return CPU的指令集
     */
    val cpuAbi: String
        get() {
            val builder = StringBuilder()
            builder.append("[")
            for (i in CPU_ABI.indices) {
                builder.append(CPU_ABI[i])
                if (i != CPU_ABI.size - 1) {
                    builder.append(",")
                }
            }
            builder.append("]")
            return builder.toString()
        }

    fun getCurrentProcessName(context: Context): String {
        val pid = Process.myPid()
        var processName = ""
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (process in manager.runningAppProcesses) {
            if (process.pid == pid) {
                processName = process.processName
            }
        }
        return processName
    }

//    fun getManifestDataByKey(context: Context, key: String): String {
//        val appInfo = App.context.packageManager
//            .getApplicationInfo(
//                context.packageName,
//                PackageManager.GET_META_DATA
//            )
//        val msg = appInfo.metaData.getString(key)
//        Log.d(TAG, "$key - $msg")
//        return msg?:""
//    }

    /**
     * 从相册中选择一张图片后， 回调Activity的onActivityResult方法进行处理
     */
    fun chooseImageFromGallery(activity: Activity, requestCode: Int) {
        val albumIntent = Intent(Intent.ACTION_PICK)
        albumIntent.type = "image/*"
        albumIntent.data = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        activity.startActivityForResult(albumIntent, requestCode)
    }

    fun openUrl(activity: Activity, url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        try {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
            log("Launching intent: " + intent + " with extras: " + intent.extras)
            activity.startActivity(intent)
        } catch (ignored: ActivityNotFoundException) {
            log("Nothing available to handle $intent")
        }

    }

    fun isChineseLang(): Boolean {
        val configuration = App.context.resources.configuration
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            return configuration.locale.language == "zh"
        } else {
            val langList = configuration.locales
            if (langList.isEmpty) {
                return true
            }
            val lang = langList[0]
            return lang.language == "zh"
        }
    }

    fun startWebView(context: Context, uri: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        }
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Sorry, Your mobile can't be supported", Toast.LENGTH_LONG)
                .show()
        }

    }

    fun getManifestDataByKey(context: Context, key: String): String {
        val appInfo = App.context.packageManager
            .getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            )
        val msg = appInfo.metaData.getString(key)
        log("$key - $msg")
        return msg ?: ""
    }

    /**
     * 获取状态栏高度
     *
     * @param context
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = 0
        val resourceId: Int =
            context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
        }

        return statusBarHeight
    }

    @SuppressLint("InvalidWakeLockTag")
    fun wakeUpAndUnlock(context: Context) {
        //屏锁管理器
        val km = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        val kl = km.newKeyguardLock("unLock")
        //解锁
        kl.disableKeyguard()
        //获取电源管理器对象
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        val wl = pm.newWakeLock(
            PowerManager.ACQUIRE_CAUSES_WAKEUP or PowerManager.SCREEN_DIM_WAKE_LOCK,
            "bright"
        )
        //点亮屏幕
        wl.acquire(10 * 60 * 1000L /*10 minutes*/)
        //释放
        wl.release()
    }

    /**
     * 是否忽略电池优化
     */
    fun isOpenPowerOptimize(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager?
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            powerManager?.isIgnoringBatteryOptimizations(context.packageName) ?: false
        } else {
            true
        }
    }

    /**
     * 跳转应用详情页面
     */
    fun jumpAppInfoPage(context: Context) {
        try {
            // 根据isOpened结果，判断是否需要提醒用户跳转AppInfo页面，去打开App通知权限
            val intent = Intent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
            } else {
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            }
            //这种方案适用于 API 26, 即8.0(含8.0)以上可以用
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, App.context.packageName)
                intent.putExtra(Notification.EXTRA_CHANNEL_ID, context.applicationInfo.uid);
            }

            //这种方案适用于 API21——25，即 5.0——7.1 之间的版本可以使用
            intent.putExtra("app_package", context.packageName);
            intent.putExtra("app_uid", context.applicationInfo.uid);

            // 小米6 -MIUI9.6-8.0.0系统，是个特例，通知设置界面只能控制"允许使用通知圆点"——然而这个玩意并没有卵用，我想对雷布斯说：I'm not ok!!!
            //  if ("MI 6".equals(Build.MODEL)) {
            //      intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            //      Uri uri = Uri.fromParts("package", getPackageName(), null);
            //      intent.setData(uri);
            //      // intent.setAction("com.android.settings/.SubSettings");
            //  }
            context.startActivity(intent);
        } catch (e: Exception) {
            e.printStackTrace();
            // 出现异常则跳转到应用设置界面：锤子坚果3——OC105 API25
            val intent = Intent();

            //下面这种方案是直接跳转到当前应用的设置界面。
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            val uri = Uri.fromParts("package", context.packageName, null);
            intent.setData(uri);
            context.startActivity(intent);
        }


//        val localIntent = Intent()
//        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
//        localIntent.data = Uri.fromParts("package", context.packageName, null)
//        context.startActivity(localIntent)
    }

    /**
     * 是否开启了通知
     */
    fun isOpenNotification() = NotificationManagerCompat.from(App.context).areNotificationsEnabled()

    private val isDebug = true
    private fun log(message: String) {
        if (isDebug) {
            Log.e("breeze", message)
        }
    }

    @TargetApi(11)
    fun enableStrictMode() {
        if (hasGingerbread()) {
            val threadPolicyBuilder = StrictMode.ThreadPolicy.Builder()
                .detectAll().penaltyLog()
            val vmPolicyBuilder = StrictMode.VmPolicy.Builder()
                .detectAll().penaltyLog()

            if (hasHoneycomb()) {
                threadPolicyBuilder.penaltyFlashScreen()
                // TODO : Add our activities later
                // vmPolicyBuilder
                // .setClassInstanceLimit(ImageGridActivity.class, 1)
                // .setClassInstanceLimit(ImageDetailActivity.class, 1);
            }
            StrictMode.setThreadPolicy(threadPolicyBuilder.build())
            StrictMode.setVmPolicy(vmPolicyBuilder.build())
        }
    }

    fun hasFroyo(): Boolean {
        // Can use static final constants like FROYO, declared in later versions
        // of the OS since they are inlined at compile time. This is guaranteed
        // behavior.
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO
    }

    fun hasGingerbread(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
    }

    fun hasHoneycomb(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB
    }

    fun hasHoneycombMR1(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1
    }

    fun hasICS(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
    }

    fun hasJellyBean(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
    }


    /**
     * 获取是否自动调节亮度
     *
     * @param context
     * @return
     */
    fun isAutoBrightness(context: Context): Boolean {
        val contentResolver = context.contentResolver

        var autoBrightness = false
        try {
            autoBrightness = Settings.System.getInt(
                contentResolver,
                Settings.System.SCREEN_BRIGHTNESS_MODE
            ) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC
        } catch (e: Settings.SettingNotFoundException) {

        }

        return autoBrightness

    }

    /**
     * 获取屏幕亮度
     *
     * @param context
     * @return
     */
    fun getBrightness(context: Context): Int {
        var brightness = 200
        try {
            brightness = Settings.System.getInt(
                context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS
            )
        } catch (e: Settings.SettingNotFoundException) {
        }

        return brightness
    }

    /**
     * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     *
     * @param context
     * @param mode
     */
    fun setBrightnessMode(context: Context, mode: Int) {
        Settings.System.putInt(
            context.contentResolver,
            Settings.System.SCREEN_BRIGHTNESS_MODE,
            mode
        )
    }

    /**
     * 设置当前屏幕亮度值 0--255，并使之生效
     */
    fun setBrightness(context: Context, bright: Int) {
        // 保存设置的屏幕亮度值
        Settings.System.putInt(context.contentResolver, Settings.System.SCREEN_BRIGHTNESS, bright)
    }

    /**
     * 保存亮度设置状态
     *
     * @param resolver
     * @param brightness
     */
    fun saveBrightness(resolver: ContentResolver, brightness: Int) {
        val uri = Settings.System
            .getUriFor("screen_brightness")
        Settings.System.putInt(
            resolver, "screen_brightness",
            brightness
        )
        resolver.notifyChange(uri, null)
    }

//
//    fun startWebView(context: Context, uri: String) {
//        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
//        } else {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
//        }
//        try {
//            context.startActivity(intent)
//        } catch (e: Exception) {
//            Toast.makeText(context, "Sorry, Your mobile can't be supported", Toast.LENGTH_LONG).show()
//        }
//
//    }

    /**
     * 获取应用详情
     *
     * @param context
     * @param packageName
     */
    fun showAppDetails(context: Context, packageName: String) {
        val mSCHEME = "package"

        /**
         * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.1及之前版本)
         */
        val appPkgName21 = "com.android.settings.ApplicationPkgName"

        /**
         * 调用系统InstalledAppDetails界面所需的Extra名称(用于Android 2.2)
         */
        val appPkgName22 = "pkg"

        /**
         * InstalledAppDetails所在包名
         */
        val aPPDefaultPackageName = "com.android.settings"

        /**
         * InstalledAppDetails类名
         */
        val aPPDefailsClassName = "com.android.settings.InstalledAppDetails"

        val intent = Intent()
        val apiLevel = Build.VERSION.SDK_INT
        if (apiLevel >= 9) {
            // 2.3（ApiLevel 9）以上，使用SDK提供的接口
            intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            val uri = Uri.fromParts(mSCHEME, packageName, null)
            intent.data = uri
        } else {
            // 2.3以下，使用非公开的接口（查看InstalledAppDetails源码）
            // 2.2和2.1中，InstalledAppDetails使用的APP_PKG_NAME不同。
            val appPkgName = if (apiLevel == 8) appPkgName22 else appPkgName21
            intent.action = Intent.ACTION_VIEW
            intent.setClassName(aPPDefaultPackageName, aPPDefailsClassName)
            intent.putExtra(appPkgName, packageName)
        }

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 检测服务是否已启动
     *
     * @param context
     * @param className
     * @return
     */
    fun isServiceRunning(context: Context, className: String): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        val list = am.getRunningServices(50)

        for (info in list) {
            if (info.service.className == className) {
                return true
            }
        }

        return false
    }

    /**
     * 检查是否安装某包
     *
     * @param context
     * @param packageName 包名
     * @return
     */
    fun isAppExist(context: Context, packageName: String): Boolean {
        try {
            context.createPackageContext(packageName, Context.CONTEXT_IGNORE_SECURITY)
        } catch (e: PackageManager.NameNotFoundException) {
            return false
        } catch (e: Exception) {
            return false
        }

        return true
    }

    /**
     * 判断程序是否安装（通过intent-filter验证）
     *
     * @param context
     * @param intent
     * @return
     */
    fun isAppExist(context: Context, intent: Intent): Boolean {
        var infos: List<ResolveInfo>? = null
        try {
            infos = context.packageManager.queryIntentActivities(intent, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return infos != null && infos.size > 0
    }

}