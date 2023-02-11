//package app.allever.android.lib.login.instagram;
//
//import android.app.Activity;
//import android.os.Environment;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.alibaba.fastjson.JSONObject;
//import com.heifeng.uni.plugin.auth.core.AccountData;
//import com.heifeng.uni.plugin.auth.core.util.ResultUtils;
//import com.heifeng.uni.plugin.auth.core.util.Utils;
//import com.taobao.weex.WXSDKEngine;
//import com.taobao.weex.annotation.JSMethod;
//import com.taobao.weex.bridge.JSCallback;
//
//import java.io.File;
//
///**
// * @author allever
// */
//public class InsPluginModule extends WXSDKEngine.DestroyableModule {
//
//    private static final String TAG = "AuthShareSDK-Ins";
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
//        String clientId = jsonObject.getString("client_id");
//        if (TextUtils.isEmpty(clientId)) {
//            String msg = "client_id 不能为空";
//            log(msg);
//            invoke(jsCallback, ResultUtils.createFailureResult(msg));
//            return;
//        }
//
//        String secret = jsonObject.getString("secret");
//        if (TextUtils.isEmpty(secret)) {
//            String msg = "secret 不能为空";
//            log(msg);
//            invoke(jsCallback, ResultUtils.createFailureResult(msg));
//            return;
//        }
//
//        String redirectUrl = jsonObject.getString("redirect_url");
//        if (TextUtils.isEmpty(redirectUrl)) {
//            String msg = "redirect_url 不能为空";
//            log(msg);
//            invoke(jsCallback, ResultUtils.createFailureResult(msg));
//            return;
//        }
//
//        InsHelper.getInstance().init(mWXSDKInstance.getContext(), clientId, "", secret, redirectUrl);
//        invoke(jsCallback, ResultUtils.createSuccessResult("初始化成功"));
//    }
//
//    @JSMethod(uiThread = true)
//    public void login(JSONObject jsonObject, final JSCallback jsCallback) {
//        if (!InsHelper.getInstance().isInstallIns(mWXSDKInstance.getContext())) {
//            invoke(jsCallback, ResultUtils.createFailureResult("Ins未安装"));
//            return;
//        }
//
//        if (mWXSDKInstance.getContext() instanceof Activity) {
//            InsHelper.getInstance().setLoginResultListener(new InsHelper.LoginResultCallback() {
//                @Override
//                public void onSuccess(InsAccount account) {
//                    //email、UserId
//                    AccountData data = new AccountData();
//                    data.setUserId(account.getUserId());
//                    data.setUserName(account.getUserName());
//                    data.setToken(account.getToken());
//                    data.setAuthCode(account.getCode());
//                    invoke(jsCallback, ResultUtils.createSuccessResult(data));
//                }
//
//                @Override
//                public void onFailure(int errorCode, String message) {
//                    invoke(jsCallback, ResultUtils.createFailureResult(message));
//                }
//            });
//            InsHelper.getInstance().login((Activity) mWXSDKInstance.getContext());
//        }
//    }
//
//    @JSMethod(uiThread = true)
//    public void shareImage(JSONObject jsonObject, JSCallback jsCallback) {
//        if (!InsHelper.getInstance().isInstallIns(mWXSDKInstance.getContext())) {
//            invoke(jsCallback, ResultUtils.createFailureResult("Ins未安装"));
//            return;
//        }
//
//        if (jsonObject == null) {
//            invoke(jsCallback, ResultUtils.createFailureResult("jsonObject 参数不能为空"));
//            return;
//        }
//
//        String filePath = jsonObject.getString("file_path");
//
//        //调试
////        String filename = "text.xin";
////        final String mediaPath = Environment.getExternalStorageDirectory() + File.separator + filename;
////        filePath = mediaPath;
////        log(filePath);
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
//            InsHelper.getInstance().shareImage((Activity) mWXSDKInstance.getContext(), filePath);
//        }
//    }
//
//    private void invoke(JSCallback jsCallback, Object object) {
//        if (jsCallback != null) {
//            jsCallback.invoke(object);
//        }
//    }
//
//    @Override
//    public void destroy() {
//    }
//
//    private void log(String msg) {
//        Log.d(TAG, msg);
//    }
//
//    private void toast(String msg) {
//        Toast.makeText(mWXSDKInstance.getContext(), msg, Toast.LENGTH_SHORT).show();
//    }
//}
