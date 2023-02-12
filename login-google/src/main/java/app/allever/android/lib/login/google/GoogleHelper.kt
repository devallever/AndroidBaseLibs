package app.allever.android.lib.login.google;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import app.allever.android.lib.core.app.App;
import app.allever.android.lib.core.function.login.Utils;

/**
 * @author allever
 *
 */
public class GoogleHelper {

    private static final String TAG = "AuthShareSDK-Google";
    private static final int RC_SIGN_IN = 0x01;

    private static final String GOOGLE_PLAY_PKG = "com.android.vending";

//    private static final String CLIENT_ID = "328468759241-rrpcgf3k29sbl9ebj31go3ciecq9d9bb.apps.googleusercontent.com";
    private GoogleSignInClient mGoogleSignInClient;
    private LoginResultCallback mLoginCallback;

    private GoogleHelper() {
    }

    private static final class Holder {
        private static final GoogleHelper INS = new GoogleHelper();
    }

    public static GoogleHelper getInstance() {
        return Holder.INS;
    }

    public void init(Context context, String appId, String appKey, String secret) {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(appId)
                .requestEmail()
                .build();

//        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions(GoogleSignInOptions.DEFAULT_SIGN_IN);

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(context, googleSignInOptions);
    }

    public void login(Activity activity) {
        if (mGoogleSignInClient != null) {
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            activity.startActivityForResult(signInIntent, RC_SIGN_IN);
        }
    }

    public void logOut(Activity activity) {
        mGoogleSignInClient.signOut().addOnCompleteListener(activity, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                log("已退出");
            }
        });
    }


    public void setLoginResultListener(LoginResultCallback callback) {
        mLoginCallback = callback;
    }

    public void handleOnActivityForResult(int requestCode, int resultCode, Intent data, Context context) {
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task, context);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask, Context context) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                log("登录成功");
                log("email = " + account.getEmail());
                log("id = " + account.getId());
                log("idToken = " + account.getIdToken());
                log("displayName = " + account.getDisplayName());
                log("ServerAuthCode = " + account.getServerAuthCode());
                log("familyName = " + account.getFamilyName());
                log("givenName = " + account.getGivenName());
                Uri headUri = account.getPhotoUrl();
                if (headUri != null) {
                    log("avatar = " + account.getPhotoUrl().toString());
                }
                toast(context, "授权成功");
                mLoginCallback.onSuccess(account);
            } else {
                String msg = "登录失败，获取不到用户信息";
                log(msg);
                toast(context, msg);
                mLoginCallback.onFailure(1, msg);
            }

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            log("登录失败");
            log("Google内部错误码：" + e.getStatusCode());
            log("错误信息：" + e.getMessage());
            e.printStackTrace();
            mLoginCallback.onFailure(e.getStatusCode(), e.getMessage());
            toast(context, "错误信息：" + e.getMessage());
        }
    }

    public boolean isLogin(Context context) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        if (account != null) {
            log("已经登录");
            log("email = " + account.getEmail());
            log("id = " + account.getId());
            log("idToken = " + account.getIdToken());
            log("displayName = " + account.getDisplayName());
            log("ServerAuthCode = " + account.getServerAuthCode());
            Uri headUri = account.getPhotoUrl();
            if (headUri != null) {
                log("avatar = " + account.getPhotoUrl().toString());
            }
            return true;
        } else {
            log("还没登录");
        }

        return false;
    }

    public void destroy() {
        mLoginCallback = null;
        mGoogleSignInClient = null;
    }

    public boolean isInstallGoogle(Context context) {
        return Utils.isInstallApp(context, GOOGLE_PLAY_PKG);
    }

    /**
     * Validates that there is a reasonable server client ID in strings.xml, this is only needed
     * to make sure users of this sample follow the README.
     */
    public void validateServerClientID(String serverClientId) {
        String suffix = ".apps.googleusercontent.com";
        if (!serverClientId.trim().endsWith(suffix)) {
            String message = "Invalid server client ID in strings.xml, must end with " + suffix;

            Log.w(TAG, message);
            toast(App.context, message);
        }
    }

    private void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    private void log(String msg) {
        Log.d(TAG, msg);
    }

    public interface LoginResultCallback {
        void onSuccess(GoogleSignInAccount account);

        void onFailure(int errorCode, String message);
    }
}
