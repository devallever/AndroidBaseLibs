//package app.allever.android.lib.login.wechat;
//
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Looper;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.alibaba.fastjson.JSONObject;
//import com.heifeng.uni.plugin.auth.core.util.ResultUtils;
//import com.heifeng.uni.plugin.auth.core.util.Utils;
//import com.taobao.weex.WXSDKEngine;
//import com.taobao.weex.annotation.JSMethod;
//import com.taobao.weex.bridge.JSCallback;
//
//import java.io.File;
//
///**
// * @author
// */
//public class WeChatModule extends WXSDKEngine.DestroyableModule {
//
//    private WeChatUtil chat;
//
//    @JSMethod(uiThread = true)
//    public void init(JSONObject jsonObject, JSCallback jsCallback) {
//        ResultUtils.init(mWXSDKInstance.getContext());
//
//        if (jsonObject == null) {
//            invoke(jsCallback, ResultUtils.createFailureResult("jsonObject 不能为空"));
//            return;
//        }
//
//        String appId = jsonObject.getString("app_id");
//        //调试
////        appId = "wx0d65fb441f7fd2ab";
//
//        if (TextUtils.isEmpty(appId)) {
//            invoke(jsCallback, ResultUtils.createFailureResult("app_id 不能为空"));
//            return;
//        }
//
//        String secret = jsonObject.getString("secret");
//        //调试
////        secret = "43d86cbb4c65b844cac5c919905ac862";
//
//        if (TextUtils.isEmpty(secret)) {
//            invoke(jsCallback, ResultUtils.createFailureResult("secret 不能为空"));
//            return;
//        }
//
//        WeChatInitialize.init(appId, secret);
//        chat = new WeChatUtil(mWXSDKInstance.getContext());
////        invoke(jsCallback, ResultUtils.createSuccessResult("初始化成功"));
//
////        if (mWXSDKInstance.getContext() == null) {
////            log("context == null");
////        }
////        Handler handler = new Handler(Looper.getMainLooper());
////        handler.postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                chat = new WeChatUtil(mWXSDKInstance.getContext());
////            }
////        }, 2000);
//
////        invoke(jsCallback, ResultUtils.createSuccessResult("初始化成功"));
//    }
//
//    @JSMethod(uiThread = true)
//    public void login(JSONObject jsonObject, final JSCallback jsCallback) {
//        if (null != chat) {
//            if (!chat.isInstallWeChat(mWXSDKInstance.getContext())) {
//                invoke(jsCallback, ResultUtils.createFailureResult("微信 未安装"));
//                return;
//            }
//
//            chat.loginToGetCode();
//            chat.setOnResultListener(new WeChatUtil.OnResultListener() {
//                @Override
//                public void onSuccess(int code, Object obj) {
//                    toast("授权成功");
//                    invoke(jsCallback, ResultUtils.createSuccessResult((String)obj));
//                }
//
//                @Override
//                public void onFailure(int code, String msg) {
//                    invoke(jsCallback, ResultUtils.createFailureResult(msg));
//                }
//            });
//        } else {
//            invoke(jsCallback, ResultUtils.createFailureResult("初始化失败"));
//        }
//    }
//
//
//    /**
//     * 微信分享
//     *
//     * @param friendsCircle 是否分享到朋友圈
//     * @param url           分享的网页地址
//     * @param bitmap        图片数据
//     * @param title         分享标题
//     * @param describe      分享内容
//     */
//    @JSMethod(uiThread = true)
//    public void shareWebUrl(JSONObject jsonObject, final JSCallback jsCallback) {
//        if (jsonObject == null) {
//            invoke(jsCallback, ResultUtils.createFailureResult("jsonObject 不能为空"));
//            return;
//        }
//
//        if (null != chat) {
//            if (!chat.isInstallWeChat(mWXSDKInstance.getContext())) {
//                invoke(jsCallback, ResultUtils.createFailureResult("微信 未安装"));
//                return;
//            }
//
//            boolean friendCircle = jsonObject.getBoolean("friendCircle");
//            String url = jsonObject.getString("url");
//            String filePath = jsonObject.getString("filePath");
//            String title = jsonObject.getString("title");
//            String description = jsonObject.getString("description");
//            chat.setOnResultListener(new WeChatUtil.OnResultListener() {
//                @Override
//                public void onSuccess(int code, Object obj) {
//                    invoke(jsCallback, ResultUtils.createSuccessResult((String) obj));
//                }
//
//                @Override
//                public void onFailure(int code, String msg) {
//                    invoke(jsCallback, ResultUtils.createResultJsonObject(code, msg));
//                }
//            });
//            chat.shareWebUrl(friendCircle, url, title, description, filePath);
//        } else {
//            invoke(jsCallback, ResultUtils.createFailureResult("微信未初始化"));
//        }
//    }
//
//    @JSMethod(uiThread = true)
//    public void shareImage(JSONObject jsonObject, final JSCallback jsCallback) {
//        if (jsonObject == null) {
//            invoke(jsCallback, ResultUtils.createFailureResult("jsonObject 不能为空"));
//            return;
//        }
//
//        if (null != chat) {
//            if (!chat.isInstallWeChat(mWXSDKInstance.getContext())) {
//                invoke(jsCallback, ResultUtils.createFailureResult("微信 未安装"));
//                return;
//            }
//
//            boolean friendCircle = jsonObject.getBoolean("friendCircle");
//            String filePath = jsonObject.getString("filePath");
//
//            //调试
////            String filename = "test.jpg";
////            final String mediaPath = Environment.getExternalStorageDirectory() + File.separator + filename;
////            filePath = mediaPath;
//
//            if (TextUtils.isEmpty(filePath)) {
//                invoke(jsCallback, ResultUtils.createFailureResult("filePath 不能为空"));
//                return;
//            }
//
//            if (!Utils.checkFile(mWXSDKInstance.getContext(), filePath)) {
//                invoke(jsCallback, ResultUtils.createFailureResult("分享图片失败"));
//                return;
//            }
//
//            chat.setOnResultListener(new WeChatUtil.OnResultListener() {
//                @Override
//                public void onSuccess(int code, Object obj) {
//                    invoke(jsCallback, ResultUtils.createSuccessResult((String) obj));
//                }
//
//                @Override
//                public void onFailure(int code, String msg) {
//                    invoke(jsCallback, ResultUtils.createResultJsonObject(code, msg));
//                }
//            });
//            chat.shareImage(friendCircle, filePath);
//        } else {
//            invoke(jsCallback, ResultUtils.createFailureResult("微信未初始化"));
//        }
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//
//    private void invoke(JSCallback jsCallback, Object object) {
//        if (jsCallback != null) {
//            jsCallback.invoke(object);
//        }
//    }
//
//    private void toast(String msg) {
//        Toast.makeText(mWXSDKInstance.getContext(), msg, Toast.LENGTH_SHORT).show();
//    }
//
//    private void log(String msg) {
//        Log.d("WechatLogin", msg);
//    }
//
//}
