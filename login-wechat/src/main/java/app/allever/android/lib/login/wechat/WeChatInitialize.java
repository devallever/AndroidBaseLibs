package app.allever.android.lib.login.wechat;

public class WeChatInitialize {

    public static String WE_CHAT_APP_ID = "";
    public static String WE_CHAT_SECRET = "";

    /**
     * 回调操作类型
     * CODE_TYPE_LOGIN 登录操作回调
     * CODE_TYPE_SHARE 分享操作回调
     * CODE_TYPE_PAY 支付操作回调
     */
    public static final int CODE_TYPE_LOGIN = 120;
    public static final int CODE_TYPE_SHARE = 121;

    private static CallBack call;

    public static void init(String appId, String secret) {
        WE_CHAT_APP_ID = appId;
        WE_CHAT_SECRET = secret;
    }

    public static void setCall(CallBack call) {
        WeChatInitialize.call = call;
    }

    public static void callBack(WeiXin info) {
        if (null != call) {
            call.call(info);
        }
    }

    public interface CallBack {
        void call(WeiXin info);
    }

}
