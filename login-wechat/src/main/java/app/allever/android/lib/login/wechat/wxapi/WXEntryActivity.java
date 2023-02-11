package app.allever.android.lib.login.wechat.wxapi;

import android.app.Activity;
import android.content.Intent;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import app.allever.android.lib.login.wechat.WeChatInitialize;
import app.allever.android.lib.login.wechat.WeiXin;

/**
 * 微信登陆分享回调Activity
 *
 * @author 安辉
 * @create time 2015-05-25
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private String TAG = WXEntryActivity.class.getSimpleName();
    private IWXAPI wxAPI;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        wxAPI = WXAPIFactory.createWXAPI(this, WeChatInitialize.WE_CHAT_APP_ID, true);
        wxAPI.registerApp(WeChatInitialize.WE_CHAT_APP_ID);
        wxAPI.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        wxAPI.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq arg0) {
        android.util.Log.e(TAG, "onReq:" + arg0);
    }

    @Override
    public void onResp(BaseResp resp) {
        // android.util.Log.e(TAG, "onResp:" + resp.getType());
        if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {//分享
            // android.util.Log.e(TAG, "微信分享操作.....");
            WeChatInitialize.callBack(new WeiXin(WeChatInitialize.CODE_TYPE_SHARE, resp.errCode, ""));
        } else if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {//登陆
            // android.util.Log.e(TAG, "微信登录操作.....");
            SendAuth.Resp authResp = (SendAuth.Resp) resp;
            WeChatInitialize.callBack(new WeiXin(WeChatInitialize.CODE_TYPE_LOGIN, resp.errCode, authResp.code));
        }
        finish();
    }

}
