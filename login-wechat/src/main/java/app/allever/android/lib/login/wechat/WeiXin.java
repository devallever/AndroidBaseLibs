package app.allever.android.lib.login.wechat;

import java.io.Serializable;

/**
 * @author ansen
 * @create time 2017-09-14
 */
public class WeiXin implements Serializable {
    private int type;//1:登录 2.分享 3:微信支付
    private int errCode;//微信返回的错误码
    private String code;//登录成功才会有的code

    public WeiXin() {
    }

    public WeiXin(int type, int errCode, String code) {
        this.type = type;
        this.errCode = errCode;
        this.code = code;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
}
