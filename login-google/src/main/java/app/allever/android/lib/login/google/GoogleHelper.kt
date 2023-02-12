package app.allever.android.lib.login.google

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import androidx.fragment.app.Fragment
import app.allever.android.lib.core.app.App
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.toast
import app.allever.android.lib.core.function.login.Utils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

/**
 * @author allever
 * @link https://developers.google.com/identity/sign-in/android/start?hl=zh-cn
 */
object GoogleHelper {

    private const val TAG = "Login-Google"
    private const val RC_SIGN_IN = 0x01
    private const val GOOGLE_PLAY_PKG = "com.android.vending"

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var mLoginCallback: LoginResultCallback? = null
    private var mLoginOption: GoogleSignInOptions? = null
    private var mClientId = ""


    /**
     * 应该只需要调用一次
     */
    fun init(context: Context, appId: String, appKey: String, secret: String) {
        //已经初始化了
        if (mLoginOption != null && appId == mClientId) {
            log("已经配置了")
            return
        }

        //配置
        config(appId)

        //初始化
        initGoogleClient(context)
    }

    private fun config(appId: String) {
        mClientId = appId
        mLoginOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(mClientId)
            .requestEmail()
            .build()
    }

    private fun initGoogleClient(context: Context) {
        if (mGoogleSignInClient == null) {
            if (mLoginOption == null) {
                toast("未初始化")
            }
            mLoginOption?.let {
                mGoogleSignInClient = GoogleSignIn.getClient(context, it)
            }
        }
    }

    fun login(activity: Activity) {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = getLoginAccount()
        if (account != null) {
            mLoginCallback?.onSuccess(account)
            log("已经登录：${account.displayName}")
            return
        }

        initGoogleClient(activity)

        val signInIntent = mGoogleSignInClient?.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun isInit() = mGoogleSignInClient != null

    private fun checkAndInit() {
        if (TextUtils.isEmpty(mClientId)) {
            return
        }
    }

    fun login(activity: Fragment) {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = getLoginAccount()
        if (account != null) {
            mLoginCallback?.onSuccess(account)
            log("已经登录：${account.displayName}")
            return
        }

        initGoogleClient(activity.requireActivity())

        val signInIntent = mGoogleSignInClient?.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun isLogin(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(App.context) != null
    }

    fun getLoginAccount() = GoogleSignIn.getLastSignedInAccount(App.context)

    fun logOut(activity: Activity, block: (() -> Unit)? = null) {
        val task = Runnable {
            log("已退出")
            block?.invoke()
        }
        if (!isLogin()) {
            task.run()
            return
        }

        initGoogleClient(activity)

        mGoogleSignInClient?.signOut()?.addOnCompleteListener(activity) {
            task.run()
        }
    }

    fun setLoginResultListener(callback: LoginResultCallback?) {
        mLoginCallback = callback
    }

    fun handleOnActivityForResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?,
    ) {
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(
                ApiException::class.java
            )
            if (account != null) {
                log("登录成功")
                log("email = " + account.email)
                log("id = " + account.id)
                log("idToken = " + account.idToken)
                log("displayName = " + account.displayName)
                log("ServerAuthCode = " + account.serverAuthCode)
                log("familyName = " + account.familyName)
                log("givenName = " + account.givenName)
                val headUri = account.photoUrl
                if (headUri != null) {
                    log("avatar = " + account.photoUrl.toString())
                }
                toast("授权成功")
                mLoginCallback?.onSuccess(account)
            } else {
                val msg = "登录失败，获取不到用户信息"
                log(msg)
                toast(msg)
                mLoginCallback?.onFailure(1, msg)
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            log("登录失败")
            log("Google内部错误码：" + e.statusCode)
            log("错误信息：" + e.message)
            e.printStackTrace()
            mLoginCallback?.onFailure(e.statusCode, e.message)
            toast("错误信息：" + e.message)
        }
    }

    fun destroy() {
        mLoginCallback = null
        mGoogleSignInClient = null
        mClientId = ""
        mLoginOption = null
    }

    fun isInstallGoogle(context: Context?): Boolean {
        return Utils.isInstallApp(context, GOOGLE_PLAY_PKG)
    }

    /**
     * Validates that there is a reasonable server client ID in strings.xml, this is only needed
     * to make sure users of this sample follow the README.
     */
    fun validateServerClientID(serverClientId: String) {
        val suffix = ".apps.googleusercontent.com"
        if (!serverClientId.trim { it <= ' ' }.endsWith(suffix)) {
            val message = "Invalid server client ID in strings.xml, must end with $suffix"
            log(message)
            toast(message)
        }
    }


    interface LoginResultCallback {
        fun onSuccess(account: GoogleSignInAccount?)
        fun onFailure(errorCode: Int, message: String?)
    }
}