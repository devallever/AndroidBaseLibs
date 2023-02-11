//package app.allever.android.lib.login.facebook;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.Looper;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.alibaba.fastjson.JSONObject;
//import com.facebook.AccessToken;
//import com.facebook.login.LoginResult;
//import com.facebook.share.Sharer;
//import com.heifeng.uni.plugin.auth.core.AccountData;
//import com.heifeng.uni.plugin.auth.core.util.ResultUtils;
//import com.heifeng.uni.plugin.auth.core.util.Utils;
//import com.taobao.weex.WXSDKEngine;
//import com.taobao.weex.annotation.JSMethod;
//import com.taobao.weex.bridge.JSCallback;
//
//import app.allever.android.lib.core.function.login.AccountData;
//import app.allever.android.lib.core.function.login.ResultUtils;
//
///**
// * @author allever
// */
//public class FacebookPluginModule extends WXSDKEngine.DestroyableModule {
//
//    private static final String TAG = "AuthShareSDK-Facebook";
//
//    private Handler mHandler = new Handler(Looper.getMainLooper());
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        FacebookHelper.getInstance().handleOnActivityForResult(requestCode, resultCode, data);
//    }
//
//    @JSMethod(uiThread = true)
//    public void init(JSONObject jsonObject, JSCallback jsCallback) {
//        log("init Facebook");
//        ResultUtils.init(mWXSDKInstance.getContext());
//
//        if (jsonObject == null) {
//            invoke(jsCallback, ResultUtils.createFailureResult("jsonObject 不能为空"));
//            return;
//        }
//
//        String client_id = jsonObject.getString("client_id");
//
////        client_id = "2627747770809974"; //zhongzhi
////        client_id = "1350542608624681";//test
//
//        if (TextUtils.isEmpty(client_id)) {
//            invoke(jsCallback, ResultUtils.createFailureResult("client_id 不能为空"));
//            return;
//        }
//
//        FacebookHelper.getInstance().init(mWXSDKInstance.getContext(), client_id, "", "");
//        invoke(jsCallback, ResultUtils.INSTANCE.createSuccessResult("初始化成功"));
//    }
//
//    @JSMethod(uiThread = true)
//    public void login(JSONObject jsonObject, final JSCallback jsCallback) {
//        if (!FacebookHelper.getInstance().isInstallFB(mWXSDKInstance.getContext())) {
//            invoke(jsCallback, ResultUtils.createFailureResult("Facebook 未安装"));
//            return;
//        }
//
//        if (FacebookHelper.getInstance().isLogin(mWXSDKInstance.getContext())) {
//            log("Facebook 已经登录");
//        } else {
//            log("Facebook 没有登录");
//        }
//
//        if (mWXSDKInstance.getContext() instanceof Activity) {
//            FacebookHelper.getInstance().setLoginResultListener(new FacebookHelper.LoginResultCallback() {
//                @Override
//                public void onSuccess(LoginResult account) {
//                    toast("授权成功");
//                    AccountData data = new AccountData();
//                    AccessToken accessToken = account.getAccessToken();
//                    data.setUserId(accessToken.getUserId());
//                    data.setToken(accessToken.getToken());
//                    invoke(jsCallback, ResultUtils.createSuccessResult(data));
//
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            FacebookHelper.getInstance().logOut();
//                        }
//                    }, 1000);
//                }
//
//                @Override
//                public void onFailure(int errorCode, String message) {
//                    invoke(jsCallback, ResultUtils.createFailureResult(message));
//                }
//            });
//            FacebookHelper.getInstance().login((Activity) mWXSDKInstance.getContext());
//        }
//    }
//
//    @JSMethod(uiThread = true)
//    public void shareWebUrl(JSONObject jsonObject, final JSCallback jsCallback) {
//        if (!FacebookHelper.getInstance().isInstallFB(mWXSDKInstance.getContext())) {
//            invoke(jsCallback, ResultUtils.createFailureResult("Facebook 未安装"));
//            return;
//        }
//
//        if (jsonObject == null) {
//            invoke(jsCallback, ResultUtils.createFailureResult("jsonObject 不能为空"));
//            return;
//        }
//
//        String url = jsonObject.getString("url");
//        if (TextUtils.isEmpty(url)) {
//            invoke(jsCallback, ResultUtils.createFailureResult("url 不能为空"));
//            return;
//        }
//
//        if (mWXSDKInstance.getContext() instanceof Activity) {
//            FacebookHelper.getInstance().setShareResultListener(new FacebookHelper.ShareResultCallback() {
//                @Override
//                public void onSuccess(Sharer.Result result) {
//                    invoke(jsCallback, ResultUtils.createSuccessResult("分享成功"));
//                }
//
//                @Override
//                public void onFailure(int errorCode, String message) {
//                    invoke(jsCallback, ResultUtils.createFailureResult(message));
//                }
//            });
//            FacebookHelper.getInstance().shareWebUrl((Activity) mWXSDKInstance.getContext(), url, "", "", null);
//        }
//    }
//
//    @JSMethod(uiThread = true)
//    public void shareImage(JSONObject jsonObject, final JSCallback jsCallback) {
//        if (!FacebookHelper.getInstance().isInstallFB(mWXSDKInstance.getContext())) {
//            invoke(jsCallback, ResultUtils.createFailureResult("Facebook 未安装"));
//            return;
//        }
//
//        if (jsonObject == null) {
//            invoke(jsCallback, ResultUtils.createFailureResult("jsonObject 不能为空"));
//            return;
//        }
//
//        String filePath = jsonObject.getString("file_path");
//
////        //调试
////        String filename = "test.jpg";
////        final String mediaPath = Environment.getExternalStorageDirectory() + File.separator + filename;
////        filePath = mediaPath;
//
//        if (TextUtils.isEmpty(filePath)) {
//            invoke(jsCallback, ResultUtils.createFailureResult("file_path 不能为空"));
//            return;
//        }
//
//        if (!Utils.checkFile(mWXSDKInstance.getContext(), filePath)) {
//            invoke(jsCallback, ResultUtils.createFailureResult("分享图片失败"));
//            return;
//        }
//
//        if (mWXSDKInstance.getContext() instanceof Activity) {
//            FacebookHelper.getInstance().setShareResultListener(new FacebookHelper.ShareResultCallback() {
//                @Override
//                public void onSuccess(Sharer.Result result) {
//                    invoke(jsCallback, ResultUtils.createSuccessResult("分享成功"));
//                }
//
//                @Override
//                public void onFailure(int errorCode, String message) {
//                    invoke(jsCallback, ResultUtils.createFailureResult(message));
//                }
//            });
//            FacebookHelper.getInstance().shareImage((Activity) mWXSDKInstance.getContext(), filePath);
//        }
//    }
//
//    @Override
//    public void destroy() {
//        FacebookHelper.getInstance().logOut();
//        FacebookHelper.getInstance().destroy();
//    }
//
//    private void invoke(JSCallback jsCallback, Object object) {
//        if (jsCallback != null) {
//            jsCallback.invoke(object);
//        }
//    }
//
//    private void log(String msg) {
//        Log.d(TAG, msg);
//    }
//
//    private void toast(String msg) {
//        Toast.makeText(mWXSDKInstance.getContext(), msg, Toast.LENGTH_SHORT).show();
//    }
//
//}
