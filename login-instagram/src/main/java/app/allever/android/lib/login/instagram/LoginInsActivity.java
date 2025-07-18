package app.allever.android.lib.login.instagram;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @author allever
 */
public class LoginInsActivity extends Activity {

    private static final String EXTRA_CLIENT_ID = "EXTRA_CLIENT_ID";
    private static final String EXTRA_SECRET = "EXTRA_SECRET";
    private static final String EXTRA_REDIRECT_URL = "EXTRA_REDIRECT_URL";

    private static final int RESPONSE_ERROR_CODE_FAILURE = 1;
    private static final int RESPONSE_ERROR_CODE_SUCCESS = 0;

    private static final String AUTH_BASE_URL = "https://www.instagram.com/oauth/authorize";

    private boolean mNeedClearCookie = false;

    //是否检查
    private boolean mNeedCheckAuthError = true;//授权请求 • Instagram   Authorization Request • Instagram

    private static final int MSG_CHECK_AUTH_ERROR = 1;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CHECK_AUTH_ERROR:
                    String url = (String) msg.obj;
                    if (mNeedCheckAuthError) {
                        checkError(url);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public static void start(Context context, String clientId, String secret, String redirectUrl) {
        Intent intent = new Intent(context, LoginInsActivity.class);
        intent.putExtra(EXTRA_CLIENT_ID, clientId);
        intent.putExtra(EXTRA_SECRET, secret);
        intent.putExtra(EXTRA_REDIRECT_URL, redirectUrl);
        context.startActivity(intent);
    }

    public static InsHelper.LoginResultCallback sLoginCallback;

    private static final String TAG = "AuthShareSDK-Ins";

    private String mRedirectUrl = "";
    private String mClientId = "";
    private String mSecret = "";

    private String mCode;
    private String mToken;

    private RequestQueue mQueue;

    private WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ins);

        Intent intent = getIntent();
        if (intent != null) {
            mClientId = getIntent().getStringExtra(EXTRA_CLIENT_ID);
            mSecret = getIntent().getStringExtra(EXTRA_SECRET);
            mRedirectUrl = getIntent().getStringExtra(EXTRA_REDIRECT_URL);
        }

        mQueue = Volley.newRequestQueue(this);

        initWebView();
    }

    @Override
    public void onBackPressed() {
        toast("授权取消");
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //每次清除缓存
        sLoginCallback = null;
        CookieManager cm = CookieManager.getInstance();
        cm.removeSessionCookie();
        cm.removeAllCookie();

//        if (mNeedClearCookie) {
//            CookieManager cm = CookieManager.getInstance();
//            cm.removeSessionCookie();
//            cm.removeAllCookie();
//        }
    }

    private void initWebView() {
        mWebView = (WebView) findViewById(R.id.webView);

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setAllowFileAccess(true);//资源加载超时操作
        if (Build.VERSION.SDK_INT > 16) {
            webSettings.setAllowFileAccessFromFileURLs(true);
        }
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
//        webSettings.setAppCacheEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NORMAL);
//        webSettings.setAppCacheMaxSize(5 * 1048576);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setKeepScreenOn(true);
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                log("onReceivedTitle title = " + title);
//                if (title.contains(" • Instagram")) {
//                    mNeedCheckAuthError = false;
//                    mHandler.removeMessages(MSG_CHECK_AUTH_ERROR);
//                } else {
//                    mNeedCheckAuthError = true;
//                }
            }
        });
        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                log("shouldOverrideUrlLoading");
                log("url = " + url);

//                if (url.startsWith(AUTH_BASE_URL)) {
//                    mNeedCheckAuthError = false;
//                } else {
//                    mNeedCheckAuthError = true;
//                }

                if (url.startsWith(mRedirectUrl)) {
                    //获取Code
                    int start = url.indexOf("code=");
                    int end = url.indexOf("#_");
                    String code = url.substring(start + 5, end);
                    log("code = " + code);
                    mCode = code;
                    getTokenAndUserId(code);
                    mNeedCheckAuthError = false;
                    mHandler.removeMessages(MSG_CHECK_AUTH_ERROR);
                    return true;
                }
                return false;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                log("onPageFinished url = " + url);
//                if (url.startsWith(AUTH_BASE_URL)) {
//                    mHandler.removeMessages(MSG_CHECK_AUTH_ERROR);
//                    Message message = new Message();
//                    message.what = MSG_CHECK_AUTH_ERROR;
//                    message.obj = url;
//                    mHandler.sendMessageDelayed(message, 3_000);
//                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                log(sslError.toString());
                sendFailureResponse("SSL Error");
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, final WebResourceError error) {
                super.onReceivedError(view, request, error);
                log("onReceivedError");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sendFailureResponse("授权失败：" + error.getErrorCode());
                        }
                    }, 2000);

                }
                mNeedClearCookie = true;
            }

            //Android 6.0 才会被调用
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, final WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                log("onReceivedHttpError");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    String url = request.getUrl().toString();
                    log("onReceivedHttpError errorUrl = " + url);
                    if (url.startsWith(AUTH_BASE_URL)) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                sendFailureResponse("授权失败：" + errorResponse.getStatusCode());
                            }
                        }, 2000);
                    }
                }
                mNeedClearCookie = true;
            }
        });

        String authUrl = AUTH_BASE_URL + "?" +
                "client_id=" + mClientId +
                "&redirect_uri=" + mRedirectUrl +
                "&scope=user_profile" +
                "&response_type=code";

        mWebView.loadUrl(authUrl);
    }

    public String convertToString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private void getTokenAndUserId(final String code) {
        final String url = "https://api.instagram.com/oauth/access_token";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                log("onResponse: = " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String token = jsonObject.getString("access_token");
                    String userId = jsonObject.getString("user_id");
                    log("token = " + token);
                    log("userId = " + userId);
                    mToken = token;
                    //已获取到 token 和 userId
                    getUserInfo(token, userId);
                } catch (Exception e) {
                    e.printStackTrace();
                    sendFailureResponse(e.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                log("onErrorResponse: ");
                error.printStackTrace();
                sendFailureResponse(error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>(8);
                map.put("client_id", mClientId);
                map.put("client_secret", mSecret);
                map.put("grant_type", "authorization_code");
                map.put("redirect_uri", mRedirectUrl);
                map.put("code", code);
                return map;
            }
        };
        mQueue.add(stringRequest);
    }

    private void getUserInfo(final String token, String userId) {
        String url = "https://graph.instagram.com/" + userId + "?fields=id,username&access_token=" + token;
        final StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                log("onResponse: = " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String userId = jsonObject.getString("id");
                    String userName = jsonObject.getString("username");
                    log("userId = " + userId);
                    log("userName = " + userName);
                    log("code = " + mCode);
                    log("token = " + mToken);
                    if (sLoginCallback != null) {
                        InsAccount account = new InsAccount();
                        account.setCode(mCode);
                        account.setToken(mToken);
                        account.setUserId(userId);
                        account.setUserName(userName);
                        sLoginCallback.onSuccess(account);
                        toast("授权成功");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    sendFailureResponse(e.getMessage());
                }
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                log("onErrorResponse: ");
                sendFailureResponse(error.getMessage());
            }
        });
        mQueue.add(stringRequest);
    }

    private void log(String msg) {
        Log.d(TAG, msg);
    }

    private void sendFailureResponse(String msg) {
        toast(msg);
        if (sLoginCallback != null) {
            sLoginCallback.onFailure(RESPONSE_ERROR_CODE_FAILURE, msg);
            finish();
        }
    }

    private void checkError(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                log("onResponse: = " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                log("onErrorResponse: ");
                mNeedClearCookie = true;
                error.printStackTrace();
                log("message = " + error);
                String msg = "";
                if (error instanceof AuthFailureError) {
                    msg = "AuthFailure";
                } else {
                    msg = "授权失败" + error.toString();
                }
                sendFailureResponse(msg);
            }
        });
        mQueue.add(stringRequest);
    }


    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
