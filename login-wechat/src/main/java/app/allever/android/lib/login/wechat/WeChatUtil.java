package app.allever.android.lib.login.wechat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;

import app.allever.android.lib.core.function.login.Utils;

/**
 * 微信工具类
 */
public class WeChatUtil implements WeChatInitialize.CallBack {

    private static final String TAG = WeChatUtil.class.getSimpleName();

    private static final String WECHAT_PKG = "com.tencent.mm";

    private IWXAPI wxAPI;
    private Context _context;
    private OnResultListener onResultListener;

    private final int MAX_TEXT_SIZE = 1024;

    public WeChatUtil(Context _context) {
        this._context = _context;
        init();
    }

    public void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }

    /**
     * 初始化工具参数
     */
    private void init() {
        if (null == WeChatInitialize.WE_CHAT_APP_ID || WeChatInitialize.WE_CHAT_APP_ID.isEmpty()) {
            throw new NullPointerException("WeiXin APPID is Empty!");
        }
        if (null == WeChatInitialize.WE_CHAT_SECRET || WeChatInitialize.WE_CHAT_SECRET.isEmpty()) {
            throw new NullPointerException("WeiXin SECRET is Empty!");
        }
        wxAPI = WXAPIFactory.createWXAPI(_context, WeChatInitialize.WE_CHAT_APP_ID, true);
        wxAPI.registerApp(WeChatInitialize.WE_CHAT_APP_ID);
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(_context, null);
        // 将该app注册到微信
        msgApi.registerApp(WeChatInitialize.WE_CHAT_APP_ID);
        WeChatInitialize.setCall(this);
    }


    /**
     * 微信登陆(三个步骤)
     * 1.微信授权登陆
     * 2.根据授权登陆code 获取该用户token
     * 3.根据token获取用户资料
     *
     * @Param isGetUserInfo true 返回授权后的token，false返回授权后的用户信息
     */
    public void loginToGetCode() {
        // 检查手机或者模拟器是否安装了微信
        Log.e("WEChat", "wxAPI.isWXAppInstalled():" + wxAPI.isWXAppInstalled());
        if (!wxAPI.isWXAppInstalled()) {
            Toast.makeText(_context, "您还未安装微信客户端", Toast.LENGTH_LONG).show();
            return;
        }
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = String.valueOf(System.currentTimeMillis());
        wxAPI.sendReq(req);
    }

    public void shareWebUrl(boolean friendsCircle, String url, String title, String describe, String filePath) {
        if (filePath == null) {
            byte[] bitmapBytes = Utils.readFile2BytesByStream(_context, null);
            shareWebUrl(friendsCircle, url, title, describe, bitmapBytes);
        } else {
            byte[] bitmapBytes = Utils.readFile2BytesByStream(_context, new File(filePath));
            shareWebUrl(friendsCircle, url, title, describe, bitmapBytes);
        }
    }

    /**
     * 微信网页分享
     *
     * @param friendsCircle 是否分享到朋友圈
     * @param url           分享的网页地址
     * @param bitmap        图片数据(封面图片缩略图byte数组)
     * @param title         分享标题
     * @param describe      分享内容
     */
    public void shareWebUrl(boolean friendsCircle, String url, String title, String describe, byte[] bitmap) {
        // 检查手机或者模拟器是否安装了微信
        Log.e("WEChat", "wxAPI.isWXAppInstalled():" + wxAPI.isWXAppInstalled());
        if (!wxAPI.isWXAppInstalled()) {
            Toast.makeText(_context, "您还未安装微信客户端", Toast.LENGTH_LONG).show();
            return;
        }
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = url;//分享url
        WXMediaMessage msg = new WXMediaMessage(webPage);
        msg.title = title;
        //这个字段好像没显示
        msg.description = TextUtils.isEmpty(describe) ? describe
                : describe.length() > MAX_TEXT_SIZE ? describe.substring(0, MAX_TEXT_SIZE - 3) + "..." : describe;
        if (null != bitmap) {
            msg.thumbData = bitmap;
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        // SendMessageToWX.Req.WXSceneSession是分享到好友会话
        // SendMessageToWX.Req.WXSceneTimeline是分享到朋友圈
        req.scene = friendsCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
        wxAPI.sendReq(req);
    }

    public void shareImage(boolean friendsCircle, String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            toast("路径不能为空");
            return;
        }
        File file = new File(filePath);
        if (!file.canRead()) {
            toast("无法读取图片");
            return;
        }

        Bitmap srcBitmap = BitmapFactory.decodeFile(filePath);
        if (srcBitmap == null) {
            toast("解析文件错");
            return;
        }

        //初始化 WXImageObject 和 WXMediaMessage 对象
        WXImageObject imgObj = new WXImageObject(srcBitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        //设置缩略图
        Bitmap thumbBmp = Bitmap.createScaledBitmap(srcBitmap, 100, 100, true);
        srcBitmap.recycle();
        msg.thumbData = Utils.bitmap2Bytes(thumbBmp);

//构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = friendsCircle ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//        req.userOpenId = getOpenId();
        //调用api接口，发送数据到微信
        wxAPI.sendReq(req);
    }

    @Override
    public void call(WeiXin info) {
        switch (info.getType()) {
            case WeChatInitialize.CODE_TYPE_LOGIN:
                //授权
                if (!TextUtils.isEmpty(info.getCode())) {
                    if (null != onResultListener) {
                        toast("授权成功");
                        onResultListener.onSuccess(0, info.getCode());
                    }
                } else {
                    if (null != onResultListener) {
                        onResultListener.onFailure(1, "授权取消");
                    }
                }
                break;
            case WeChatInitialize.CODE_TYPE_SHARE:
                //分享
                switch (info.getErrCode()) {
                    case BaseResp.ErrCode.ERR_OK:
                        //分享成功
                        if (null != onResultListener) {
                            onResultListener.onSuccess(0, info.getCode());
                            Log.d(TAG, "微信分享成功: ");
//                            Toast.makeText(_context, "分享成功", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        //分享取消
                        if (null != onResultListener) {
                            onResultListener.onFailure(1, "微信分享取消.....");
                            Log.d(TAG, "微信分享取消: ");
                            Toast.makeText(_context, "分享取消", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case BaseResp.ErrCode.ERR_AUTH_DENIED:
                        //分享被拒绝
                        if (null != onResultListener) {
                            onResultListener.onFailure(-1, "微信分享被拒绝.....");
                            Log.d(TAG, "微信分享被拒绝: ");
                            Toast.makeText(_context, "分享失败", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    private void toast(String msg) {
        Toast.makeText(_context, msg, Toast.LENGTH_SHORT).show();
    }

    public boolean isInstallWeChat(Context context) {
        return Utils.isInstallApp(context, WECHAT_PKG);
    }

    public interface OnResultListener {
        void onSuccess(int code, Object obj);

        void onFailure(int code, String msg);
    }
}
