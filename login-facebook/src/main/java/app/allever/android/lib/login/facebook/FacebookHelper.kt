package app.allever.android.lib.login.facebook

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import app.allever.android.lib.core.ext.log
import app.allever.android.lib.core.ext.toast
import app.allever.android.lib.core.function.login.Utils
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.CallbackManager.Factory.create
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk.sdkInitialize
import com.facebook.FacebookSdk.setAdvertiserIDCollectionEnabled
import com.facebook.FacebookSdk.setApplicationId
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.share.Sharer
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareDialog
import java.io.File

/**
 * https://developers.facebook.com/docs/facebook-login/android
 */
object FacebookHelper {

    private const val TAG = "Login-Facebook"

    private const val FB_PKG = "com.facebook.katana"

    private var mCallbackManager: CallbackManager? = null

    private var mLoginCallback: LoginResultCallback? = null

    private var mShareDialog: ShareDialog? = null

    private var mShareCallback: ShareResultCallback? = null

    fun init(context: Context?, appId: String?, appKey: String?, secret: String?) {
        //插件形式 要在Manifest配置
        setApplicationId(appId!!)
        sdkInitialize(context!!)
        setAdvertiserIDCollectionEnabled(false)
        mCallbackManager = create()
        LoginManager.getInstance()
            .registerCallback(mCallbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    // App code
                    val accessToken = loginResult.accessToken
                    val token = accessToken.token
                    log("登录成功")
                    log("token = $token")
                    log("userId = " + accessToken.userId)
                    log("graphDomain = " + accessToken.graphDomain)
                    toast("授权成功")
                    mLoginCallback?.onSuccess(loginResult)
                }

                override fun onCancel() {
                    // App code
                    val msg = "授权取消"
                    log(msg)
                    if (mLoginCallback != null) {
                        toast(msg)
                        mLoginCallback?.onFailure(1, msg)
                    }
                }

                override fun onError(exception: FacebookException) {
                    // App code
                    val msg = "授权失败: " + exception.message
                    log(msg)
                    exception.printStackTrace()
                    if (mLoginCallback != null) {
                        toast(msg)
                        mLoginCallback?.onFailure(1, msg)
                    }
                }
            })
    }

    fun login(activity: Activity) {
        LoginManager.getInstance().logInWithReadPermissions(activity, listOf("public_profile"))
    }

    fun logOut() {
        LoginManager.getInstance().logOut()
    }

    fun shareWebUrl(
        activity: Activity?,
        url: String?,
        title: String?,
        describe: String?,
        bitmap: ByteArray?
    ) {
        if (mShareDialog == null) {
            mShareDialog = ShareDialog(activity!!)
            mShareDialog?.registerCallback(
                mCallbackManager!!,
                object : FacebookCallback<Sharer.Result> {
                    override fun onSuccess(result: Sharer.Result) {
                        log("分享成功")
                        if (mShareCallback != null) {
                            toast("分享成功")
                            mShareCallback?.onSuccess(result)
                        }
                    }

                    override fun onCancel() {
                        val msg = "分享取消"
                        log(msg)
                        if (mShareCallback != null) {
                            toast(msg)
                            mShareCallback?.onFailure(1, msg)
                        }
                    }

                    override fun onError(error: FacebookException) {
                        val msg = "分享失败: 错误信息 = " + error.message
                        log(msg)
                        error.printStackTrace()
                        mShareCallback?.onFailure(1, error.message)
                    }
                })
        }
        val content: ShareLinkContent = ShareLinkContent.Builder()
            .setContentUrl(Uri.parse(url))
            .build()
        if (mShareDialog?.canShow(content) == true) {
            mShareDialog?.show(content)
        }
    }

    fun shareImage(activity: Activity?, filePath: String?) {
        if (mShareDialog == null) {
            mShareDialog = ShareDialog(activity!!)
            mShareDialog!!.registerCallback(
                mCallbackManager!!,
                object : FacebookCallback<Sharer.Result> {
                    override fun onSuccess(result: Sharer.Result) {
                        log("分享成功")
                        if (mShareCallback != null) {
                            toast("分享成功")
                            mShareCallback?.onSuccess(result)
                        }
                    }

                    override fun onCancel() {
                        val msg = "分享取消"
                        log(msg)
                        if (mShareCallback != null) {
                            toast(msg)
                            mShareCallback?.onFailure(1, msg)
                        }
                    }

                    override fun onError(error: FacebookException) {
                        val msg = "分享失败: 错误信息 = " + error.message
                        log(msg)
                        error.printStackTrace()
                        if (mShareCallback != null) {
                            mShareCallback?.onFailure(1, error.message)
                        }
                    }
                })
        }
        if (TextUtils.isEmpty(filePath)) {
            toast("filePath 不能为空")
            return
        }
        val file = File(filePath)
        if (!file.exists()) {
            toast("文件不存在")
            return
        }
        if (!file.canRead()) {
            toast("无法读取文件")
            return
        }
        val bytes = Utils.readFile2BytesByStream(activity, file)
        var bitmap: Bitmap? = null
        if (bytes != null) {
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
        }
        val photo: SharePhoto = SharePhoto.Builder()
            .setBitmap(bitmap)
            .build()
        val content: SharePhotoContent = SharePhotoContent.Builder()
            .addPhoto(photo)
            .build()
        if (mShareDialog?.canShow(content) == true) {
            mShareDialog?.show(content)
        }
    }

    fun setLoginResultListener(callback: LoginResultCallback) {
        mLoginCallback = callback
    }

    fun setShareResultListener(callback: ShareResultCallback) {
        mShareCallback = callback
    }

    fun handleOnActivityForResult(requestCode: Int, resultCode: Int, data: Intent?) {
        mCallbackManager!!.onActivityResult(requestCode, resultCode, data)
    }

    fun isLogin(context: Context?): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }

    fun getToken(context: Context?): String? {
        return if (isLogin(context)) {
            AccessToken.getCurrentAccessToken()?.token
        } else ""
    }

    fun getCallbackManager(): CallbackManager? {
        return mCallbackManager
    }

    fun isInstallFB(context: Context?): Boolean {
        return Utils.isInstallApp(context, FB_PKG)
    }

    fun destroy() {}

    interface LoginResultCallback {
        fun onSuccess(account: LoginResult?)
        fun onFailure(errorCode: Int, message: String?)
    }

    interface ShareResultCallback {
        fun onSuccess(result: Sharer.Result)
        fun onFailure(errorCode: Int, message: String?)
    }
}