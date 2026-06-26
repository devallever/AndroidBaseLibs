# 🔥🔥🔥Android基础组件库(停更)🔥🔥🔥

> ⚠️ 该组件已停止更新，请使用[AndroidBusinessBaseLibs](https://github.com/devallever/AndroidBusinessBaseLibs)

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

## 常用控件

### RefreshRecyclerView

封装SmartRefreshLayout + RecyclerView, 一行代码实现实现下拉刷新、上拉加载更多，预加载，分页加载
```kotlin
refreshRV.setAdapter(mAdapter)
    .dataFetchListener(object : RefreshRecyclerView.DataFetchListener<UserItem> {
        override fun loadData(currentPage: Int, isLoadMore: Boolean) {
            loadUser(currentPage, isLoadMore)
        }

        override suspend fun fetchData(
                currentPage: Int,
                isLoadMore: Boolean
        ): MutableList<UserItem> {
            return fetchUser(currentPage, isLoadMore)
        }
    })
    .enableViewPager(false)
    .enableRefresh(true)
    .pageChangeListener(object : RefreshRecyclerView.PageChangeListener<UserItem> {
        override fun onPageChanged(position: Int, item: UserItem) {
            toast("position = $position, item = $item")
        }
    })
    .execute()
```