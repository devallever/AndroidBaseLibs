//package app.allever.android.lib.login.google;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import com.alibaba.fastjson.JSONObject;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.heifeng.uni.plugin.auth.core.AccountData;
//
///**
// * @author allever
// */
//public class TestActivity extends Activity {
//
//    private static final String TAG = "GoogleSignIn";
//    //UniPluginDemo
////    private static final String CLIENT_ID = "328468759241-rrpcgf3k29sbl9ebj31go3ciecq9d9bb.apps.googleusercontent.com";
//
//    //WebClientId - com.heifeng.sample.uni.plugin.demo
////    private static final String CLIENT_ID = "328468759241-mk3jkka1eh1vmafl8ffgsr075u2tsbfe.apps.googleusercontent.com";
//
//    //AndroidClientId - uni.UNI03661D3
////    private static final String CLIENT_ID = "746568146308-ekgvbplal0lspr54i52c8digltokuv3c.apps.googleusercontent.com";
//
//    //WebClientId - uni.UNI03661D3
//    private static final String CLIENT_ID = "746568146308-rt3dt3tc2gogkgbqqv66q62thi36vvri.apps.googleusercontent.com";
//
//
//    //ZhongZhi
//    //    private static final String CLIENT_ID = "";
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.test_activity);
//
//        findViewById(R.id.btnSignIn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                signIn();
//            }
//        });
//
//
//        AccountData data = new AccountData();
//        JSONObject object = new JSONObject();
//        object.put("code", 0);
//        object.put("data", data);
//        Log.d(TAG, "result = " + object.toString());
//    }
//
//
//    private void signIn() {
//        validateServerClientID();
////        GoogleHelper.getInstance().init(this, getString(R.string.server_client_id), "", "");
//        GoogleHelper.getInstance().init(this, CLIENT_ID, "", "");
//        GoogleHelper.getInstance().setLoginResultListener(new GoogleHelper.LoginResultCallback() {
//            @Override
//            public void onSuccess(GoogleSignInAccount account) {
//
//            }
//
//            @Override
//            public void onFailure(int errorCode, String message) {
//
//            }
//        });
//        boolean isLogin = GoogleHelper.getInstance().isLogin(this);
//        if (!isLogin) {
//            GoogleHelper.getInstance().login(this);
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        GoogleHelper.getInstance().handleOnActivityForResult(requestCode, resultCode, data, this);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        //todo 退出
//        GoogleHelper.getInstance().logOut(this);
//    }
//
//    /**
//     * Validates that there is a reasonable server client ID in strings.xml, this is only needed
//     * to make sure users of this sample follow the README.
//     */
//    public void validateServerClientID(String serverClientId) {
//        String suffix = ".apps.googleusercontent.com";
//        if (!serverClientId.trim().endsWith(suffix)) {
//            String message = "Invalid server client ID in strings.xml, must end with " + suffix;
//
//            Log.w(TAG, message);
//            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
//        }
//    }
//}
