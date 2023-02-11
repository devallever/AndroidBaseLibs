//package app.allever.android.lib.login.wechat;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.View;
//
//import androidx.annotation.Nullable;
//
//import java.io.File;
//
//public class TestActivity extends Activity {
//
//    private static final String TAG = TestActivity.class.getSimpleName();
//    //微信app isApkDebugable
//    //ewuyou
////    public static final String WECHAT_APP_ID = "wxe9c148a0f8b1331c";
////    public static final String WECHAT_APP_SECRET = "da76cd942d9edfdee5aad9c631e0f239";
//
//    public static final String WECHAT_APP_ID = "wx0d65fb441f7fd2ab";
//    public static final String WECHAT_APP_SECRET = "43d86cbb4c65b844cac5c919905ac862";
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
//
//        WeChatInitialize.init(WECHAT_APP_ID, WECHAT_APP_SECRET);
//        final WeChatUtil weChatUtil = new WeChatUtil(this.getApplicationContext());
//        weChatUtil.setOnResultListener(new WeChatUtil.OnResultListener() {
//            @Override
//            public void onSuccess(int code, Object obj) {
//                Log.d(TAG, "onSuccess: "+ obj);
//            }
//
//            @Override
//            public void onFailure(int code, String msg) {
//                Log.d(TAG, "onFailure: ");
//            }
//        });
//
//
//        String filename = "test.jpg";
//        final String mediaPath = Environment.getExternalStorageDirectory() + File.separator + filename;
//        log(mediaPath);
//
//
//        findViewById(R.id.btnShareImage).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                weChatUtil.shareImage(true, mediaPath);
//            }
//        });
//
//        findViewById(R.id.btnShareUrl).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                weChatUtil.shareWebUrl(true, "https://baidu.com", "Test", "", mediaPath);
//            }
//        });
//
//        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                weChatUtil.setOnResultListener(new WeChatUtil.OnResultListener() {
//                    @Override
//                    public void onSuccess(int code, Object obj) {
//                        Log.d(TAG, "登录 onSuccess: "+ obj);
//                    }
//
//                    @Override
//                    public void onFailure(int code, String msg) {
//                        Log.d(TAG, "登录 onFailure: ");
//                    }
//                });
//                weChatUtil.loginToGetCode();
//            }
//        });
//
////        weChatUtil.shareWebUrl(true, "https://www.baidu.com/", "Title", "desc", null);
//    }
//
//    private void log(String msg) {
//        Log.d(TAG, msg);
//    }
//}
