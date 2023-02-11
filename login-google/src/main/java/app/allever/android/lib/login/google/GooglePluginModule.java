//package app.allever.android.lib.login.google;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Handler;
//import android.os.Looper;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.alibaba.fastjson.JSONObject;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.heifeng.uni.plugin.auth.core.AccountData;
//import com.heifeng.uni.plugin.auth.core.util.ResultUtils;
//import com.taobao.weex.WXSDKEngine;
//import com.taobao.weex.annotation.JSMethod;
//import com.taobao.weex.bridge.JSCallback;
//
///**
// * @author allever
// */
//public class GooglePluginModule extends WXSDKEngine.DestroyableModule {
//
//    private static final String TAG = "AuthShareSDK-Google";
//
//    private Handler mHandler = new Handler(Looper.getMainLooper());
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
//
//        //调试
//        //clientId = "746568146308-rt3dt3tc2gogkgbqqv66q62thi36vvri.apps.googleusercontent.com";
//
//        if (TextUtils.isEmpty(clientId)) {
//            String msg = "client_id 不能为空";
//            log(msg);
//            invoke(jsCallback, ResultUtils.createFailureResult(msg));
//            return;
//        }
//
//        GoogleHelper.getInstance().init(mWXSDKInstance.getContext(), clientId, "", "");
//        invoke(jsCallback, ResultUtils.createSuccessResult("初始化成功"));
//    }
//
//    @JSMethod(uiThread = true)
//    public void login(JSONObject jsonObject, final JSCallback jsCallback) {
//        if (! GoogleHelper.getInstance().isInstallGoogle(mWXSDKInstance.getContext())) {
//            invoke(jsCallback, ResultUtils.createFailureResult("谷歌服务 未安装"));
//            return;
//        }
//        log("pkgName = " + mWXSDKInstance.getContext().getPackageName());
//        if (mWXSDKInstance.getContext() instanceof Activity) {
//            GoogleHelper.getInstance().setLoginResultListener(new GoogleHelper.LoginResultCallback() {
//                @Override
//                public void onSuccess(GoogleSignInAccount account) {
//                    if (account == null) {
//                        invoke(jsCallback, ResultUtils.createFailureResult("授权失败"));
//                        return;
//                    }
//
//                    AccountData data = new AccountData();
//                    data.setUserId(account.getId());
//                    Uri headUri = account.getPhotoUrl();
//                    if (headUri != null) {
//                        data.setUserHeadUrl(account.getPhotoUrl().toString());
//                    }
//                    data.setEmail(account.getEmail());
//                    data.setToken(account.getIdToken());
//                    data.setFullName(account.getDisplayName());
//                    data.setFamilyName(account.getFamilyName());
//                    data.setGivenName(account.getGivenName());
//                    toast("授权成功");
//                    invoke(jsCallback, ResultUtils.createSuccessResult(data));
//
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            GoogleHelper.getInstance().logOut((Activity) mWXSDKInstance.getContext());
//                        }
//                    }, 1000);
//                }
//
//                @Override
//                public void onFailure(int errorCode, String message) {
//                    invoke(jsCallback, ResultUtils.createFailureResult(message));
//                }
//            });
//            GoogleHelper.getInstance().login((Activity) mWXSDKInstance.getContext());
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
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        GoogleHelper.getInstance().handleOnActivityForResult(requestCode, resultCode, data, mWXSDKInstance.getContext());
//    }
//
//    @Override
//    public void destroy() {
//        if (mWXSDKInstance.getContext() instanceof Activity) {
//            GoogleHelper.getInstance().logOut((Activity) mWXSDKInstance.getContext());
//        }
//        GoogleHelper.getInstance().destroy();
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
