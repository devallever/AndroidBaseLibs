# 使用kotlin协程封装Retrofit的网络请求框架

> Github: [https://github.com/devallever/AndroidBaseLibs](https://github.com/devallever/AndroidBaseLibs)


使用Kotlin协程封装Retrofit, 配置网络请求，添加自定义缓存，统一错误处理，扩展性强

## 使用示例

- 配置网络请求: 可配置url，拦截器，请求头，成功状态码，
```
HttpConfig.baseUrl("https://www.wanandroid.com/")
    .interceptor(GlobalInterceptor())
    .networkInterceptor(GlobalNetInterceptor())
    .header("KEY","VALUE")
    .successCode(0)
    .build()
```

- 调用请求

kotlin
```
mainCoroutine.launch {
    val result = NetRepository.getBanner(BannerResponseCache())
    result?.let {
        tvResult.text = GsonHelper.toJson(it)
    }
}
```

java

```
NetRepository.INSTANCE.getBannerCall(new BannerResponseCache(), new ResponseCallback<List<BannerData>>() {
    @Override
    public void onFail(@NonNull NetResponse<List<BannerData>> response) {
        LogHelper.INSTANCE.d(response.getMsg());
        textView.setText(GsonHelper.INSTANCE.toJson(response));
    }

    @Override
    public void onSuccess(@NonNull NetResponse<List<BannerData>> response) {
        LogHelper.INSTANCE.d(response.getMsg());
        textView.setText(GsonHelper.INSTANCE.toJson(response));
    }
});
```

## 实现原理(Kotlin)
> java 调用方式不同，流程和原理都一样

### 网络配置：HttpConfig

- 通过build模式构建网络配置
- 简化配置，减少模版代码
- 内置了一些通用拦截器, 日志/请求头部
- successCode可能每个人都不同，单独抽取出来

```
HttpConfig.baseUrl("https://www.wanandroid.com/")
    .interceptor(GlobalInterceptor())
    .networkInterceptor(GlobalNetInterceptor())
    .header("KEY","VALUE")
    .successCode(0)
    .build()
```

配置后在**ApiService**类创建Retrofit实例时读取配置

```
    private val mRetrofit by lazy {

        val builder = OkHttpClient.Builder()

        //日志拦截
        val loggingInterceptor = HttpLoggingInterceptor(LoggerInterceptor())
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        builder.addInterceptor(loggingInterceptor)
        builder.addInterceptor(HttpInterceptor())

        //添加外部拦截器
        HttpConfig.interceptors.map {
            builder.addInterceptor(it)
        }

        HttpConfig.networkInterceptors.map {
            builder.addNetworkInterceptor(it)
        }

        //缓存容量
        val SIZE_OF_CACHE = (100 * 1024 * 1024).toLong() // 100 MB

        //缓存路径
        val cacheFile: String = App.context.externalCacheDir.toString() + "/http"
        val cache = Cache(File(cacheFile), SIZE_OF_CACHE)
        builder.cache(cache)

        //请求头拦截
        builder.addInterceptor(HeaderInterceptor())
        Retrofit.Builder()
            .client(builder.build())
            .baseUrl(HttpConfig.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
    }
```

- 通过ApiService#get() 方法获取接口实例
```
fun <T> get(apiServiceClass: Class<T>): T = mRetrofit.create(apiServiceClass)

private val wanAndroidApi by lazy {
    ApiService.get(WanAndroidApi::class.java)
}
```

### 流程

- 创建接口方法, 使用了挂起函数，直接返回response，不需要另外封装

```
@GET("banner/json")
suspend fun getBanner(): BaseResponse<List<BannerData>>
```

- NetRepository网络层仓库，作为统一调用入口

```
suspend fun getBanner(responseCache: ResponseCache<*>? = null) =
    request(BaseResponse::class.java, responseCache) {
    wanAndroidApi.getBanner()
}
```

- NetRepository继承**NetworkHandler**，NetworkHandler统一网络发起网络请求和返回处理


- 流程
   1.判断是否使用缓存（网络不可用或开启缓存的情况下才读取缓存）
   2.不使用缓存或获取缓存失败则调用网络请求
   3.请求成功后自动缓存
   4.返回网络响应
   5.统一异常处理

```
    /**
     * kotlin协程方式请求
     * @param responseClz 响应体的class类型
     * @param responseCache 缓存类，null表示不使用缓存
     * @param block 高阶函数，执行相应都网络请求
     */
    inline fun <T : NetResponse<*>> request(
        responseClz: Class<*>?,
        responseCache: ResponseCache<*>? = null,
        block: () -> T
    ): T? {
        try {
            //1.先判断是否读取缓存
            if (!NetworkHelper.isNetworkAvailable(App.context) || responseCache != null) {
                //网络不可用或开启缓存的情况下才读取缓存
                val response = responseCache?.getCache<T>()
                response?.let {
                    log("使用缓存: ${response.data}")
                    return response
                }
            }

            //2.不使用缓存或获取缓存失败则调用网络请求
            val response = block()
            //3.请求成功后自动缓存
            if (HttpCode.isSuccessCode(response.getCode())) {
                //缓存响应
                responseCache?.cacheResponse(response)
            }
            //4. 返回网络响应
            return response
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            loge(e.message)
            //5.统一错误处理
            val exception = ExceptionHandle.handleException(e)
            val response = responseClz?.newInstance()
            log("responseClz = ${response?.javaClass?.simpleName}")
            if (response is NetResponse<*>) {
                response.setData(exception.code, exception.message, null)
            }
            return response as? T?
        }
    }
```

### 缓存

- 缓存抽象类：ResponseCache，缓存的是NetResponse的实现类的json数据
- 常规的添加缓存/删除缓存/获取缓存方法
- 通过继承方式实现每个请求接口的缓存，自定义缓存key和缓存时间（默认5分钟）
```
class BannerResponseCache: ResponseCache<BaseResponse<List<BannerData>>>() {
    override fun cacheKey(): String {
        return "banner"
    }
}
```

- 通过范型设计，解除具体缓存类的耦合

```
abstract class ResponseCache<T : NetResponse<*>> {

    private fun saveCache(result: T) {
        putStringCache(cacheKey(), toJson(result))
        log("缓存response：${toJson(result)}")
        putCacheTime(cacheTimeKey(), System.currentTimeMillis())
    }

    private fun deleteCache() {
        putCache(cacheKey(), null)
        putStringCache(cacheKey(), "")
    }

    fun cacheResponse(response: NetResponse<*>) {
        saveCache(response as T)
    }

    /**
     * 获取缓存
    */
    fun <T> getCache(): T? {
        //1. 判断是否超时
        if (System.currentTimeMillis() - getCacheTime(cacheTimeKey()) >= cacheTime()
            && NetworkHelper.isNetworkAvailable(App.context)
        ) {
            deleteCache()
            return null
        }

        try {
            val cacheString = getStringCache(cacheKey())
            val response = Gson().fromJson(
                cacheString,
                HttpDataUtils.getGenericType(this::class.java, 0)
            ) as? T
            response.let {
                log("获取缓存: $cacheString")
                return response
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    /**
     * 缓存时间
     *
     * 默认缓存5分钟
     *
     * @return
     */
    protected open fun cacheTime(): Long {
        return 1000 * 60 * 5
    }

    /**
    * 缓存时间key，内部实现即可
    */
    private fun cacheTimeKey(): String {
        return cacheKey() + "_cache_time"
    }

    /**
     * 每个接口的缓存key
     * @return
     */
    abstract fun cacheKey(): String
}
```

### 统一异常/错误处理

- ResultThrowable封装错误码和错误信息
- ExceptionHandle 解析异常并创建对应的ResultThrowable

```
        try {
            //...
            //网络请求
            val response = block()
            //...
            return response
        } catch (e: java.lang.Exception) {
            //...
            val exception = ExceptionHandle.handleException(e)
            val response = responseClz?.newInstance()
            if (response is NetResponse<*>) {
                response.setData(exception.code, exception.message, null)
            }
            return response as? T?
        }
```

```
    public static ResultThrowable handleException(Throwable e) {
        ResultThrowable ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResultThrowable(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case FORBIDDEN:
                    ex.message = getStringRes(R.string.request_failed);
                    break;
                case NEXT_FOUND:
                    ex.message = getStringRes(R.string.next_action_prompt);
                    break;
                case NOT_FOUND:
                    ex.message = getStringRes(R.string.interface_does_not_exist);
                    break;
                case INTERNAL_SERVER_ERROR:
                    ex.message = getStringRes(R.string.network_unavailable);
                    break;
                case SERVICE_UNAVAILABLE:
                    ex.message = getStringRes(R.string.server_unavailable);
                    break;
                default:
                    ex.message = getStringRes(R.string.network_error);
                    break;
            }
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException
                || e instanceof MalformedJsonException) {
            ex = new ResultThrowable(e, ERROR.PARSE_ERROR);
            ex.message = getStringRes(R.string.parsing_error);
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResultThrowable(e, ERROR.NETWORD_ERROR);
            ex.message = getStringRes(R.string.connection_failed);
            return ex;
        } else if (e instanceof javax.net.ssl.SSLException) {
            ex = new ResultThrowable(e, ERROR.SSL_ERROR);
            ex.message = getStringRes(R.string.certificate_verification_failed);
            return ex;
        } else if (e instanceof ConnectTimeoutException || e instanceof java.net.SocketTimeoutException) {
            ex = new ResultThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = getStringRes(R.string.connection_timed_out);
            return ex;
        } else if (e instanceof java.net.UnknownHostException) {
            try {
                ex = new ResultThrowable(e, ERROR.TIMEOUT_ERROR);
            } catch (Exception es) {
                ex = new ResultThrowable(es, ERROR.TIMEOUT_ERROR);
            }
            ex.message = getStringRes(R.string.host_address_unknown);
            return ex;
        } else {
            ex = new ResultThrowable(e, ERROR.UNKNOWN);
            ex.message = getStringRes(R.string.unknown_mistake);
            return ex;
        }
    }
```

### 响应体基类设计

- NetResponse是所有响应体的基类
```
abstract class NetResponse<DATA> {
    var data: DATA? = null

    abstract fun getCode(): Int
    abstract fun getMsg(): String

    abstract fun setData(code: Int, msg: String, data: DATA?)

}
```

- 考虑到每个接口msg和code的字段可能不一样，抽象两个方法由子类自行实现字段，并通过抽象方法获取字段值, 或者添加额外的字段，扩展性强!!


```
open class BaseResponse<DATA> : NetResponse<DATA>() {
    var errorCode: Int = 0
    var errorMsg: String = ""
    //可自行添加
    var pageCount: Int = 0
    var pageSize: Int = 20

    override fun getCode() = errorCode
    override fun getMsg() = errorMsg

    override fun setData(code: Int, msg: String, data: DATA?) {
        this.errorCode = code
        this.errorMsg = msg
        this.data = data
    }
}
```








