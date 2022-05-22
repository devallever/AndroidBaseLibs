package app.allever.android.lib.network;

/**
 * @ClassName RsThrowable
 * @Description TODO
 * @Author Jerry
 * @Date 2020/5/4 0:17
 * @Version 1.0
 */
public class ResultThrowable extends Exception {

    public int code;
    public String message;

    public ResultThrowable(Throwable cause, int code) {
        super(cause);
        this.code = code;
    }

    public ResultThrowable(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
