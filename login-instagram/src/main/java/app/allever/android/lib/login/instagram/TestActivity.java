//package app.allever.android.lib.login.instagram;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.support.annotation.Nullable;
//import android.support.v4.content.FileProvider;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//
//import java.io.File;
//
///**
// * @author allever SocializySDK
// */
//public class TestActivity extends Activity {
//
//    private ImageView mImageView;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);
//
//        mImageView = (ImageView) findViewById(R.id.imageView);
//
//        InsHelper.getInstance().init(this,
//                "1061807414235317",
//                "",
//                "976158457b124be31fe7412325b60462",
//                "https://github.com/devallever");
//
//        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InsHelper.getInstance().setLoginResultListener(new InsHelper.LoginResultCallback() {
//                    @Override
//                    public void onSuccess(InsAccount account) {
//                        log("onSuccess: Ins 授权成功");
//                    }
//
//                    @Override
//                    public void onFailure(int errorCode, String message) {
//                        log("onSuccess: Ins 授权失败");
//                    }
//                });
//                InsHelper.getInstance().login(TestActivity.this);
//            }
//        });
//
//        findViewById(R.id.btnShare).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String filename = "test.jpg";
//                String mediaPath = Environment.getExternalStorageDirectory() + File.separator + filename;
//                log(mediaPath);
//                InsHelper.getInstance().shareImage(TestActivity.this, mediaPath);
//            }
//        });
//    }
//
//
//
//    private void log(String msg) {
//        Log.d("Ins Login", msg);
//    }
//
//    private Intent getShareTextIntent(String msg) {
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
//        shareIntent.setType("text/plain");
//        return Intent.createChooser(
//                shareIntent,
//                "Share to");
//    }
//}
