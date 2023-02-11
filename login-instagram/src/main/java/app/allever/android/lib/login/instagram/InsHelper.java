package app.allever.android.lib.login.instagram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;


import androidx.core.content.FileProvider;

import java.io.File;

import app.allever.android.lib.core.function.login.Utils;

/**
 * @author allever
 */
public class InsHelper {

    private static final String TAG = "AuthShareSDK-Ins";

    private String mClientId;
    private String mSecret;
    private String mRedirectUrl;

    private LoginResultCallback mCallback;

    private static final String INS_PKG = "com.instagram.android";

    /***
     *
     * @param context
     * @param appId Ins的Client
     * @param appKey 可空
     * @param secret Ins的Secret
     */
    public void init(Context context, String appId, String appKey, String secret) {
        init(context, appId, appKey, secret, "");
    }

    /***
     *
     * @param context
     * @param appId
     * @param appKey
     * @param secret
     * @param redirectUrl
     */
    public void init(Context context, String appId, String appKey, String secret, String redirectUrl) {
        mClientId = appId;
        mSecret = secret;
        mRedirectUrl = redirectUrl;
    }

    public void login(Activity activity) {
        if (!isInstallIns(activity)) {
            toast(activity, "未安装Ins");
            return;
        }
        LoginInsActivity.sLoginCallback = mCallback;
        LoginInsActivity.start(activity, mClientId, mSecret, mRedirectUrl);
    }

    public void shareWebUrl(Activity activity, String url, String title, String describe, byte[] bitmap) {
        //只能分享图片
    }

    public void shareImage(Activity activity, String filePath) {
        if (!isInstallIns(activity)) {
            toast(activity, "未安装Ins");
            return;
        }
        activity.startActivity(createInsIntent(activity, filePath));
    }

    public void setLoginResultListener(LoginResultCallback callback) {
        mCallback = callback;
    }

    private Intent createInsIntent(Context context, String mediaPath) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setPackage("com.instagram.android");
        share.setType("image/*");
        File media = new File(mediaPath);
        if (!media.exists()) {
            toast(context, "文件不存在");
            log("文件不存在");
            return null;
        }

        if (!media.canRead()) {
            toast(context, "不能读取文件，请检查权限");
            log("不能读取文件，请检查权限");
            return null;
        }

        Uri uri = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //如果没有配置FileProvider会报错
                uri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileprovider", media);
            } else {
                uri = Uri.fromFile(media);
            }
            share.putExtra(Intent.EXTRA_STREAM, uri);
        } catch (Exception e) {
            e.printStackTrace();
            toast(context, "获取文件uri失败");
        }

        return share;
    }

    private void log(String msg) {
        Log.d(TAG, msg);
    }

    private void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public boolean isInstallIns(Context context) {
        return Utils.isInstallApp(context, INS_PKG);
    }

    private InsHelper() {
    }

    private static class Holder {
        private static final InsHelper INS = new InsHelper();
    }

    public static InsHelper getInstance() {
        return Holder.INS;
    }

    public interface LoginResultCallback {
        void onSuccess(InsAccount account);

        void onFailure(int errorCode, String message);
    }

}
