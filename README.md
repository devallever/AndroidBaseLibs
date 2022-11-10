# 🔥🔥🔥Android基础组件库🔥🔥🔥

打造一个简单易用的基础组件库，封装架构组件，网络组件，数据存储组件，图片加载组件，媒体库组件，运行时权限组件，全局捕获异常组件，
业务流程组件，日志组件，广告组件

## 架构组件

### MVVM架构
```kotlin
class MvvmActivity : BaseMvvmActivity<ActivityMvvmBinding, MainViewModel>() {
    override fun getMvvmConfig() = MvvmConfig(R.layout.activity_mvvm, BR.mainViewModel)

    override fun init() {
        mViewModel.login()
    }
}
```

### MVP架构
```kotlin
class MvpActivity : BaseMvpActivity<MainView, MainPresenter, ActivityMvpBinding>(), MainView {
    override fun bindView(inflater: LayoutInflater) = ActivityMvpBinding.inflate(inflater)
    override fun getPresenter() = MainPresenter()

    override fun init() {
        mPresenter.login()
    }

    override fun updateUsername(username: String) {
        mBinding.tvUsername.text = username
    }
}
```

## 网络组件

- 一行代码实现网络请求
```kotlin
//返回数据类
val result = NetRepository.getBanner(BannerResponseCache())
if (result.success()) {
    //处理数据
    bannerMultiLiveData.value = result
} else {
    toast(result.errorMsg)
}

//返回LiveData
lifecycleScope.launch {
    NetRepository.getBannerForLiveData(BannerResponseCache()).value?.let {
        updateUi(it)
    }
}
```

## 数据存储组件

```kotlin
//设置存储实现方案, 默认使用SharePreference实现，可通过实现IDataStore接口，替换存储方案
DataStore.init(MMKVStore)

interface IDataStore {

    fun putInt(key: String, value: Int)
    fun getInt(key: String): Int

    fun putFloat(key: String, value: Float)
    fun getFloat(key: String): Float

    fun putDouble(key: String, value: Double)
    fun getDouble(key: String): Double

    fun putString(key: String, value: String)
    fun getString(key: String): String

    fun putBoolean(key: String, value: Boolean)
    fun getBoolean(key: String): Boolean
    fun getBoolean(key: String, default: Boolean): Boolean

    fun putParcelable(key: String, value: Parcelable)
    fun <T : Parcelable> getParcelable(key: String, clz: Class<T>): T?

}
```

## 图片加载组件

```kotlin
//设置加载引擎，默认Coil
ImageLoader.init(this, GlideLoader, ImageLoader.Builder.create())

binding.iv1.load(resId)
binding.iv1.load(url)
//圆形图片
binding.iv2.loadCircle(url2)
//圆角图片
binding.iv3.loadRound(url3, 10F)
//高斯模糊
binding.iv4.loadBlur(url4, 10F)
//圆形图片，边框
binding.iv5.loadCircle(url5, 2, Color.parseColor("#ff6c1e"))
//下载图片
lifecycleScope.launch {
    downloadImg(url6) { _, bitmap ->
        bitmap?.let {
            (binding.iv6).setImageBitmap(BitmapFactory.decodeFile(it.absolutePath))
        }
    }
}
binding.iv7.load(R.drawable.ic_icon_gift)
//Gif
binding.iv8.loadGif(R.drawable.ic_gif)
```

## 媒体库组件

一行代码获取音频/视频/图片资源
```kotlin
val imageList = MediaHelper.getImageMedia(context, dir)
val videoList = MediaHelper.getVideoMedia(context, dir)
val musicList = MediaHelper.getAudioMedia(context, dir)
```

一行代码实现图片/视频/音频选择
```kotlin
lifecycleScope.launch {
    val result = MediaPickerHelper.launchPicker(this@MainActivity,
        MediaPickerHelper.TYPE_IMAGE,
        MediaPickerHelper.TYPE_VIDEO,
        MediaPickerHelper.TYPE_AUDIO
    )
    handleResult(result)
}
```

## 权限组件

一行代码实现申请权限业务, 为什么请求？跳转设置，总是拒绝

```kotlin
//设置权限方案, 默认
PermissionHelper.init(DefaultPermissionEngine)


PermissionHelper.requestPermission(
    object : PermissionListener {
        override fun onAllGranted() {
            toast("全部同意")
        }

        //是否申请权限前提示为什么要权限
        override fun needShowWhyRequestPermissionDialog(): Boolean {
            return true
        }

        //总是拒绝后是否弹窗跳转设置
        override fun needShowJumpSettingDialog(): Boolean {
            return true
        }

        //自定义"为什么要权限"弹窗
        override fun getWhyRequestPermissionDialog(): Dialog? {
            return super.getWhyRequestPermissionDialog()
        }
    }, Manifest.permission.CAMERA,
    Manifest.permission.READ_PHONE_STATE
)

```

## 全局捕获异常组件

```kotlin

Cockroach.install(this, mExceptionHandler)

private val mExceptionHandler: ExceptionHandler by lazy {
    object : ExceptionHandler() {
        override fun onUncaughtExceptionHappened(thread: Thread?, throwable: Throwable?) {
            logE("CrashHandler onUncaughtExceptionHappened")
            logE("CrashHandler", "--->onUncaughtExceptionHappened:$thread<---")
            toastDebug(throwable?.message)
        }

        override fun onBandageExceptionHappened(throwable: Throwable?) {
            toastDebug(throwable?.message)
            logE("CrashHandler onBandageExceptionHappened")
//                throwable.printStackTrace() //打印警告级别log，该throwable可能是最开始的bug导致的，无需关心
        }

        override fun onEnterSafeMode() {
            toastDebug("onEnterSafeMode")
            logE("CrashHandler onEnterSafeMode")
        }

        override fun onMayBeBlackScreen(e: Throwable?) {
            toastDebug("onMayBeBlackScreen")
            logE("CrashHandler onMayBeBlackScreen")
//                val thread: Thread = Looper.getMainLooper().getThread()
//                Log.e("AndroidRuntime", "--->onUncaughtExceptionHappened:$thread<---")
//                //黑屏时建议直接杀死app
//                sysExcepHandler.uncaughtException(thread, RuntimeException("black screen"))
        }
    }
}
```

## 业务流程组件

责任链模式实现业务拦截器

```kotlin
InterceptChain.create()
    .attach(this)
    .addIntercept(FirstIntoInterceptor())
    .addIntercept(SecIntoInterceptor())
    .addIntercept(ThirdInterceptor())
    .build()
    .process()
```

## 日志组件

```kotlin
log(msg)
logE(msg)
logReleaseD(msg)
logReleaseE(msg)
```

## 广告链组件

一行代码实现加载广告
```kotlin

AdChainHelper.loadAd()

fun loadAd(adName: String, container: ViewGroup?, adChainListener: AdChainListener?) {
    //根据adName获取广告的类型
    val adConfigBean = nameConfigMap[adName]
    val adEngine = AdEngine(adName, adConfigBean, container)
    adEngine.loadAd(adChainListener)
}
```

```kotlin
//配置
object AdContract {
    private val XIAO_MI_APP_ID = if (BuildConfig.DEBUG) {
        "appId"
    } else {
        "appId"
    }

    // 以下两个没有的话就按照以下传入
    const val XIAO_MI_APP_KEY = "fake_app_key"
    const val XIAO_MI_APP_TOKEN = "fake_app_token"

    val INSERT = if (BuildConfig.DEBUG) {
        "adKey"
    } else {
        "adKey"
    }

    val MAIN_BACK_INSERT = if (BuildConfig.DEBUG) {
        "adKey"
    } else {
        "adKey"
    }
    val ADVANCED_SETTING_ENTER_INSERT = if (BuildConfig.DEBUG) {
        "adKey"
    } else {
        "229775d13b912d0273f885272b25c45a"
    }
    val SELECTED_RINGTONE_INSERT = if (BuildConfig.DEBUG) {
        "adKey"
    } else {
        "16fcbe7ee15cf0eb90d532ac57999302"
    }
    val SETTING_INSERT = if (BuildConfig.DEBUG) {
        "1d576761b7701d436f5a9253e7cf9572"
    } else {
        "cb1b9b797149f3aea9a1ff5f422ce04e"
    }
    val SELECTED_WALL_PAGER_INSERT = if (BuildConfig.DEBUG) {
        "adKey"
    } else {
        "076c4500a1c03bd47f0994c92e52517e"
    }

    val MAIN_BANNER = if (BuildConfig.DEBUG) {
        "adKey"
    } else {
        "4cea409bf739019cfac2cde8822bb1f9"
    }

    val GUIDE_BANNER = if (BuildConfig.DEBUG) {
        "adKey"
    } else {
        "fdbeb0ec7989f21ab477e9a914d99280"
    }

    val SETTING_BANNER = if (BuildConfig.DEBUG) {
        "adKey"
    } else {
        "da0a4151248552a7684079f90eba203a"
    }

    val ADVANCED_SETTING_BANNER = if (BuildConfig.DEBUG) {
        "adKey"
    } else {
        "eaf3136d01cc3a1e2ae0ae48db6f4d4a"
    }

    val SUPPORT_ENCOURAGE = if (BuildConfig.DEBUG) {
        "92d90db71791e6b9f7caaf46e4a997ec"
    } else {
        "65d6c82cad78449debfdbb7b7595087e"
    }
    
    //adeverdeng
    val ADMOB_APP_ID = "account\nca-app-pub-xxx~xxx"
    private const val ADMOB_MAIN_BANNER = "ca-app-pub-xxx/xxx"
    private const val ADMOB_GUIDE_BANNER = "ca-app-pub-xxx/xxx"
    private const val ADMOB_SETTING_BANNER = "ca-app-pub-xxx/xxx"
    private const val ADMOB_ADVANCED_SETTING_BANNER = "ca-app-pub-xxx/xxx"
    private const val ADMOB_EXIT_INSERT = "ca-app-pub-xxx/xxx"
    private const val ADMOB_BACK_MAIN_INSERT = "ca-app-pub-xxx/xxx"
    private const val ADMOB_SETTING_INSERT = "ca-app-pub-xxx/2045706461"
    private const val ADMOB_SETTING_VIDEO = "ca-app-pub-xxx/xxx"
    private const val ADMOB_COMMON_NATIVE = "ca-app-pub-xxx/xxx"
    
    val AD_NAME_EXIT_INSERT = "AD_NAME_EXIT_INSERT"

    val AD_NAME_MAIN_BACK_INSERT = "AD_NAME_MAIN_BACK_INSERT"

    val AD_NAME_SETTING_INSERT = "AD_NAME_SETTING_INSERT"

    val AD_NAME_ADVANCED_SETTING_INSERT = "AD_NAME_ADVANCED_SETTING_INSERT"

    val AD_NAME_SELECTED_RINGTONE_INSERT = "AD_NAME_SELECTED_RINGTONE_INSERT"

    val AD_NAME_SELECTED_WALL_PAGER_INSERT = "AD_NAME_SELECTED_WALL_PAGER_INSERT"

    val AD_NAME_MAIN_BANNER = "AD_NAME_MAIN_BANNER"

    val AD_NAME_SETTING_BANNER = "AD_NAME_SETTING_BANNER"

    val AD_NAME_GUIDE_BANNER = "AD_NAME_GUIDE_BANNER"

    val AD_NAME_ADVANCED_SETTING_BANNER = "AD_NAME_ADVANCED_SETTING_BANNER"

    val AD_NAME_SETTING_ENCOURAGE_VIDEO = "AD_NAME_SETTING_ENCOURAGE_VIDEO"

    val AD_NAME_COMMON_NATIVE = "AD_NAME_COMMON_NATIVE"

    val AD_NAME_COMMON_NATIVE_SMALL = "AD_NAME_COMMON_NATIVE_SMALL"


    val adData = "{\n" +
            "  \"business\": [\n" +
            "    {\n" +
            "      \"name\": \"${AdBusiness.A}\",\n" +
            "      \"appId\": \"\",\n" +
            "      \"appKey\": \"\",\n" +
            "      \"token\": \"\"\n" +
            "    },\n" +
            "   {\n" +
            "      \"name\": \"${AdBusiness.MI}\",\n" +
            "      \"appId\": \"$XIAO_MI_APP_ID\",\n" +
            "      \"appKey\": \"$XIAO_MI_APP_KEY\",\n" +
            "      \"token\": \"$XIAO_MI_APP_TOKEN\"\n" +
            "    }" +
            "  ],\n" +
            "  \"adConfig\": [\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_EXIT_INSERT\",\n" +
            "      \"type\": \"${ADType.INSERT}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_EXIT_INSERT\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$INSERT\"\n" +
            "        }" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_MAIN_BACK_INSERT\",\n" +
            "      \"type\": \"${ADType.INSERT}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_BACK_MAIN_INSERT\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$MAIN_BACK_INSERT\"\n" +
            "        }" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_SETTING_INSERT\",\n" +
            "      \"type\": \"${ADType.INSERT}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_SETTING_INSERT\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$SETTING_INSERT\"\n" +
            "        }" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_ADVANCED_SETTING_INSERT\",\n" +
            "      \"type\": \"${ADType.INSERT}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_SETTING_INSERT\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$ADVANCED_SETTING_ENTER_INSERT\"\n" +
            "        }" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_SELECTED_RINGTONE_INSERT\",\n" +
            "      \"type\": \"${ADType.INSERT}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$SELECTED_RINGTONE_INSERT\"\n" +
            "        }" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_SELECTED_WALL_PAGER_INSERT\",\n" +
            "      \"type\": \"${ADType.INSERT}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$SELECTED_WALL_PAGER_INSERT\"\n" +
            "        }" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_MAIN_BANNER\",\n" +
            "      \"type\": \"${ADType.BANNER}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_MAIN_BANNER\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$MAIN_BANNER\"\n" +
            "        }" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_SETTING_BANNER\",\n" +
            "      \"type\": \"${ADType.BANNER}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_SETTING_BANNER\"\n" +
            "        }," +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$SETTING_BANNER\"\n" +
            "        }" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_GUIDE_BANNER\",\n" +
            "      \"type\": \"${ADType.BANNER}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_GUIDE_BANNER\"\n" +
            "        }," +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$GUIDE_BANNER\"\n" +
            "        }" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_ADVANCED_SETTING_BANNER\",\n" +
            "      \"type\": \"${ADType.BANNER}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_ADVANCED_SETTING_BANNER\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$ADVANCED_SETTING_BANNER\"\n" +
            "        }" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_COMMON_NATIVE\",\n" +
            "      \"type\": \"${ADType.NATIVE}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_COMMON_NATIVE\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_COMMON_NATIVE_SMALL\",\n" +
            "      \"type\": \"${ADType.NATIVE_SMALL}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_COMMON_NATIVE\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"$AD_NAME_SETTING_ENCOURAGE_VIDEO\",\n" +
            "      \"type\": \"${ADType.VIDEO}\",\n" +
            "      \"chain\": [\n" +
            "        {\n" +
            "          \"business\": \"${AdBusiness.A}\",\n" +
            "          \"adPosition\": \"$ADMOB_SETTING_VIDEO\"\n" +
            "        }," +
            "        {\n" +
            "          \"business\": \"${AdBusiness.MI}\",\n" +
            "          \"adPosition\": \"$SUPPORT_ENCOURAGE\"\n" +
            "        }" +
            "      ]\n" +
            "    }\n" +
            "  ]\n" +
            "}\n"
}

```

```kotlin
class AdFactory : IAdBusinessFactory {
    override fun getAdBusiness(businessName: String): IAdBusiness? {
        return when (businessName) {
            AdBusiness.A -> {
                AdMobBusiness
            }
            AdBusiness.MI -> {
                MiMoBusiness
            }
            else -> {
                null
            }
        }
    }
}
```