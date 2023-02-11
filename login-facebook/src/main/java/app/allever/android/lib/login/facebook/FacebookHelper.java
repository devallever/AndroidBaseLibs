package app.allever.android.lib.login.facebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import java.io.File;
import java.util.Collections;

import app.allever.android.lib.core.function.login.Utils;

/**
 * @author allever
 */
public class FacebookHelper {

    private static final String TAG = "AuthShareSDK-Facebook";

    private static final String FB_PKG = "com.facebook.katana";

    private CallbackManager mCallbackManager;

    private LoginResultCallback mLoginCallback;

    private ShareDialog mShareDialog;

    private ShareResultCallback mShareCallback;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private FacebookHelper() {
    }

    private static class Holder {
        private static final FacebookHelper INS = new FacebookHelper();
    }

    public static FacebookHelper getInstance() {
        return Holder.INS;
    }

    public void init(final Context context, String appId, String appKey, String secret) {
        //插件形式 要在Manifest配置
        FacebookSdk.setApplicationId(appId);
        FacebookSdk.sdkInitialize(context);
        FacebookSdk.setAdvertiserIDCollectionEnabled(false);
        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                AccessToken accessToken = loginResult.getAccessToken();
                String token = accessToken.getToken();
                log("登录成功");
                log("token = " + token);
                log("userId = " + accessToken.getUserId());
                log("graphDomain = " + accessToken.getGraphDomain());
                toast(context, "授权成功");
                if (mLoginCallback != null) {
                    mLoginCallback.onSuccess(loginResult);
                }
            }

            @Override
            public void onCancel() {
                // App code
                String msg = "授权取消";
                log(msg);
                if (mLoginCallback != null) {
                    toast(context, msg);
                    mLoginCallback.onFailure(1, msg);
                }
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                String msg = "授权失败: " + exception.getMessage();
                log(msg);
                exception.printStackTrace();
                if (mLoginCallback != null) {
                    toast(context, msg);
                    mLoginCallback.onFailure(1, msg);
                }
            }
        });
    }

    public void login(Activity activity) {
        LoginManager.getInstance().logInWithReadPermissions(activity, Collections.singletonList("public_profile"));
    }

    public void logOut() {
        LoginManager.getInstance().logOut();
    }

    public void shareWebUrl(final Activity activity, String url, String title, String describe, byte[] bitmap) {
        if (mShareDialog == null) {
            mShareDialog = new ShareDialog(activity);
            mShareDialog.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    log("分享成功");
                    if (mShareCallback != null) {
                        toast(activity, "分享成功");
                        mShareCallback.onSuccess(result);
                    }
                }

                @Override
                public void onCancel() {
                    String msg = "分享取消";
                    log(msg);
                    if (mShareCallback != null) {
                        toast(activity, msg);
                        mShareCallback.onFailure(1, msg);
                    }
                }

                @Override
                public void onError(FacebookException error) {
                    String msg = "分享失败: 错误信息 = " + error.getMessage();
                    log(msg);
                    error.printStackTrace();
                    if (mShareCallback != null) {
                        mShareCallback.onFailure(1, error.getMessage());
                    }
                }
            });
        }

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(url))
                .build();

        if (mShareDialog.canShow(content)) {
            mShareDialog.show(content);
        }
    }

    public void shareImage(final Activity activity, String filePath) {
        if (mShareDialog == null) {
            mShareDialog = new ShareDialog(activity);
            mShareDialog.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    log("分享成功");
                    if (mShareCallback != null) {
                        toast(activity, "分享成功");
                        mShareCallback.onSuccess(result);
                    }
                }

                @Override
                public void onCancel() {
                    String msg = "分享取消";
                    log(msg);
                    if (mShareCallback != null) {
                        toast(activity, msg);
                        mShareCallback.onFailure(1, msg);
                    }
                }

                @Override
                public void onError(FacebookException error) {
                    String msg = "分享失败: 错误信息 = " + error.getMessage();
                    log(msg);
                    error.printStackTrace();
                    if (mShareCallback != null) {
                        mShareCallback.onFailure(1, error.getMessage());
                    }
                }
            });
        }

        if (TextUtils.isEmpty(filePath)) {
            toast(activity, "filePath 不能为空");
            return;
        }

        File file = new File(filePath);
        if (!file.exists()) {
            toast(activity, "文件不存在");
            return;
        }

        if (!file.canRead()) {
            toast(activity, "无法读取文件");
            return;
        }

        byte[] bytes = Utils.readFile2BytesByStream(activity, file);
        Bitmap bitmap = null;
        if (bytes != null) {
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }

        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        if (mShareDialog.canShow(content)) {
            mShareDialog.show(content);
        }
    }

    public void setLoginResultListener(LoginResultCallback callback) {
        mLoginCallback = callback;
    }

    public void setShareResultListener(ShareResultCallback callback) {
        mShareCallback = callback;
    }

    public void handleOnActivityForResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isLogin(Context context) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && !accessToken.isExpired();
    }

    public String getToken(Context context) {
        if (isLogin(context)) {
            return AccessToken.getCurrentAccessToken().getToken();
        }
        return "";
    }

    public CallbackManager getCallbackManager() {
        return mCallbackManager;
    }

    public boolean isInstallFB(Context context) {
        return Utils.isInstallApp(context, FB_PKG);
    }

    public void destroy() {
    }

    private void log(String msg) {
        Log.d(TAG, msg);
    }

    private void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public interface LoginResultCallback {
        void onSuccess(LoginResult account);

        void onFailure(int errorCode, String message);
    }

    public interface ShareResultCallback {
        void onSuccess(Sharer.Result result);
        void onFailure(int errorCode, String message);
    }
}
