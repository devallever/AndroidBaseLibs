//package app.allever.android.lib.network.cache;
//
//
//import android.os.Parcelable;
//import android.text.TextUtils;
//import android.util.Log;
//
//
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.util.List;
//
//import app.allever.android.lib.core.app.App;
//import app.allever.android.lib.core.helper.ExecutorHelper;
//import app.allever.android.lib.core.helper.NetworkHelper;
//import app.allever.android.lib.network.BuildConfig;
//import app.allever.android.lib.network.GsonHelper;
//import app.allever.android.lib.network.ParameterizedTypeImpl;
//import app.allever.android.lib.network.response.NetResponse;
//
//
//public abstract class CacheNetModel<DATA> {
//
////    private CacheListener<DATA> listener;
//    private boolean useCache = true;
//    public CacheNetModel() {
//    }
//
//
////    public CacheNetModel(CacheListener<DATA> listener) {
////        this.listener = listener;
////    }
////
////    public void load() {
////        getCache(listener);
////    }
//
//    public void load(CacheListener<DATA> cacheCallback) {
////        listener = cacheCallback;
//        getCache(cacheCallback);
//    }
//
//    public void setUseCache(boolean useCache) {
//        this.useCache = useCache;
//    }
//
//    /**
//     * 默认缓存5分钟
//     *
//     * @return
//     */
//    protected long cacheTime() {
//        return 1000 * 60 * 5;
//    }
//
//    protected void cacheResponse(NetResponse<DATA> response, CacheListener<DATA> cacheListener) {
//        DATA result = response.getData();
//        if (isSuccessCode(response.getCode()) && result != null) {
//            ExecutorHelper.INSTANCE.getCacheExecutor().execute(() -> {
//                saveCache(result);
//                App.mainHandler.post(() -> {
//                    cacheListener.onSuccess(result);
//                });
//            });
//
//        } else {
//            cacheListener.onFailed(response.getCode(), response.getMsg());
//        }
//    }
//
//    private void saveCache(DATA result) {
//        if (result instanceof Parcelable) {
//            HttpCacheManager.INSTANCE.putCache(cacheKey(), (Parcelable) result);
//        } else {
//            HttpCacheManager.INSTANCE.putStringCache(cacheKey(), GsonHelper.INSTANCE.toJson(result));
//        }
//        HttpCacheManager.INSTANCE.putCacheTime(cacheTimeKey(), System.currentTimeMillis());
//    }
//
//    private void deleteCache() {
//        HttpCacheManager.INSTANCE.putCache(cacheKey(), null);
//        HttpCacheManager.INSTANCE.putStringCache(cacheKey(), "");
//    }
//
//    protected void getCache(CacheListener<DATA> cacheListener) {
//        boolean networkAvailable = NetworkHelper.INSTANCE.isNetworkAvailable(App.context);
//        //缓存时间
//        if (System.currentTimeMillis() - HttpCacheManager.INSTANCE.getCacheTime(cacheTimeKey()) >= cacheTime()
//                || networkAvailable || !useCache) {
//            deleteCache();
//            requestApi(cacheListener);
//            return;
//        }
//
//        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericSuperclass();
//        Type type = parameterizedType.getActualTypeArguments()[0];
//
//        if (type instanceof Class) {
//            Class clz = (Class) type;
//            DATA data = (DATA) HttpCacheManager.INSTANCE.getCache(cacheKey(), clz);
//            if (data != null) {
//                log("getCache: 读对象缓存");
//                cacheListener.onSuccess(data);
//                return;
//            }
//        }
//
//        if (type instanceof ParameterizedType) {
//            type = ((ParameterizedType) type).getActualTypeArguments()[0];
//            if (type instanceof Class) {
//                Class clz = (Class) type;
//                String cacheString = HttpCacheManager.INSTANCE.getStringCache(cacheKey());
//                if (!TextUtils.isEmpty(cacheString)) {
//                    //Gson解析泛型: https://www.jianshu.com/p/4f797b1f8011
//                    // 传泛型的Type和我们想要的外层类的Type来组装我们想要的类型
//                    Type ty = new ParameterizedTypeImpl(List.class, new Class[]{clz});
//                    DATA data = GsonHelper.INSTANCE.getGson().fromJson(cacheString, ty);
//                    log("getCache: 读字符缓存");
//                    cacheListener.onSuccess(data);
//                    return;
//                }
//            }
//
//            log("getCache: 不使用缓存");
//            requestApi(cacheListener);
//        }
//    }
//
//    private String cacheTimeKey() {
//        return cacheKey() + "_cache_time";
//    }
//
//    private void log(String msg) {
//        if (BuildConfig.DEBUG) {
//            Log.d("CacheNetModel", msg);
//        }
//    }
//
//    private boolean isSuccessCode(int code) {
//        return code == 200;
//    }
//
//    /**
//     *
//     * @return
//     */
//    public abstract String cacheKey();
//
//    /**
//     *
//     * @param cacheListener
//     */
//    public abstract void requestApi(CacheListener<DATA> cacheListener);
//}
