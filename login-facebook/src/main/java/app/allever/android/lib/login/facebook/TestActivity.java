//package app.allever.android.lib.login.facebook;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import com.facebook.login.LoginResult;
////import com.facebook.login.widget.LoginButton;
//import com.facebook.login.widget.LoginButton;
//import com.facebook.share.Sharer;
//
//import java.io.File;
//
///**
// * @author allever
// */
//public class TestActivity extends Activity {
//
//    private static final String TAG = "FacebookPlugin";
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
//
////        LoginButton btnLogin = (LoginButton) findViewById(R.id.btnLogin);
//
//          String client_id = "2627747770809974"; //zhongzhi
////        String client_id = "1350542608624681";//test
//
//        FacebookHelper.getInstance().init(this, client_id, "", "");
//        FacebookHelper.getInstance().setLoginResultListener(new FacebookHelper.LoginResultCallback() {
//            @Override
//            public void onSuccess(LoginResult account) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(TestActivity.this, "Test 授权成功 UI", Toast.LENGTH_SHORT).show();
//                    }
//                });
//                Toast.makeText(TestActivity.this, "Test 授权成功", Toast.LENGTH_SHORT).show();
//                log("onSuccess: ");
//            }
//
//            @Override
//            public void onFailure(int errorCode, String message) {
//                log("onFailure: ");
//            }
//        });
//
//        // Callback registration
////        btnLogin.registerCallback(FacebookHelper.getInstance().getCallbackManager(), new FacebookCallback<LoginResult>() {
////            @Override
////            public void onSuccess(LoginResult loginResult) {
////                // App code
////                String token = loginResult.getAccessToken().getToken();
////                log("登录成功");
////                log("token = " + token);
////            }
////
////            @Override
////            public void onCancel() {
////                // App code
////                log("登录取消");
////            }
////
////            @Override
////            public void onError(FacebookException exception) {
////                // App code
////                log("登录失败: " + exception.getMessage());
////                exception.printStackTrace();
////            }
////        });
//
//        findViewById(R.id.btnLoginFB).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                if (FacebookHelper.getInstance().isLogin(TestActivity.this)) {
////                    log("已经登录");
////                    String token = FacebookHelper.getInstance().getToken(TestActivity.this);
////                    log("token = " + token);
////                } else {
////                    log("没有登录");
////                    FacebookHelper.getInstance().login(TestActivity.this);
////                }
//                FacebookHelper.getInstance().login(TestActivity.this);
//            }
//        });
//
//        String filename = "test.jpg";
//        final String mediaPath = Environment.getExternalStorageDirectory() + File.separator + filename;
//        log(mediaPath);
//
//        findViewById(R.id.btnShareUrl).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FacebookHelper.getInstance().setShareResultListener(new FacebookHelper.ShareResultCallback() {
//                    @Override
//                    public void onSuccess(Sharer.Result result) {
//                        log("Success: ");
//                    }
//
//                    @Override
//                    public void onFailure(int errorCode, String message) {
//                        log("onFailure: ");
//                    }
//                });
//                FacebookHelper.getInstance().shareWebUrl(TestActivity.this, "http://www.baidu.com", "", "", null);
//            }
//        });
//
//        findViewById(R.id.btnShareImage).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                FacebookHelper.getInstance().setShareResultListener(new FacebookHelper.ShareResultCallback() {
//                    @Override
//                    public void onSuccess(Sharer.Result result) {
//                        log("分享图片Success: ");
//                    }
//
//                    @Override
//                    public void onFailure(int errorCode, String message) {
//                        log("分享图片onFailure: ");
//                    }
//                });
//                FacebookHelper.getInstance().shareImage(TestActivity.this, mediaPath);
//            }
//        });
//
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        FacebookHelper.getInstance().handleOnActivityForResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        FacebookHelper.getInstance().logOut();
//    }
//
//    private void log(String msg) {
//        Log.d(TAG, msg);
//    }
//}
