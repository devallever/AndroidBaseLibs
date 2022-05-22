package app.allever.android.lib.network;

import android.net.ParseException;

import androidx.annotation.StringRes;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;

import java.net.ConnectException;

import app.allever.android.lib.core.app.App;
import retrofit2.HttpException;

/**
 * Created by goldze on 2017/5/11.
 */
public class ExceptionHandle {

    public static final int FORBIDDEN = 400;
    public static final int NEXT_FOUND = 201;
    public static final int NOT_FOUND = 404;
    public static final int INTERNAL_SERVER_ERROR = 500;
    public static final int SERVICE_UNAVAILABLE = 503;

    public static ResultThrowable handleException(Throwable e) {
        ResultThrowable ex;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            ex = new ResultThrowable(e, ERROR.HTTP_ERROR);
            switch (httpException.code()) {
                case FORBIDDEN:
                    ex.message = getStringRes(R.string.request_failed);
                    break;
                case NEXT_FOUND:
                    ex.message = getStringRes(R.string.next_action_prompt);
                    break;
                case NOT_FOUND:
                    ex.message = getStringRes(R.string.interface_does_not_exist);
                    break;
                case INTERNAL_SERVER_ERROR:
                    ex.message = getStringRes(R.string.network_unavailable);
                    break;
                case SERVICE_UNAVAILABLE:
                    ex.message = getStringRes(R.string.server_unavailable);
                    break;
                default:
                    ex.message = getStringRes(R.string.network_error);
                    break;
            }
            return ex;
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException
                || e instanceof MalformedJsonException) {
            ex = new ResultThrowable(e, ERROR.PARSE_ERROR);
            ex.message = getStringRes(R.string.parsing_error);
            return ex;
        } else if (e instanceof ConnectException) {
            ex = new ResultThrowable(e, ERROR.NETWORD_ERROR);
            ex.message = getStringRes(R.string.connection_failed);
            return ex;
        } else if (e instanceof javax.net.ssl.SSLException) {
            ex = new ResultThrowable(e, ERROR.SSL_ERROR);
            ex.message = getStringRes(R.string.certificate_verification_failed);
            return ex;
        } else if (e instanceof ConnectTimeoutException || e instanceof java.net.SocketTimeoutException) {
            ex = new ResultThrowable(e, ERROR.TIMEOUT_ERROR);
            ex.message = getStringRes(R.string.connection_timed_out);
            return ex;
        } else if (e instanceof java.net.UnknownHostException) {
            try {
                ex = new ResultThrowable(e, ERROR.TIMEOUT_ERROR);
            } catch (Exception es) {
                ex = new ResultThrowable(es, ERROR.TIMEOUT_ERROR);
            }
            ex.message = getStringRes(R.string.host_address_unknown);
            return ex;
        } else {
            ex = new ResultThrowable(e, ERROR.UNKNOWN);
            ex.message = getStringRes(R.string.unknown_mistake);
            return ex;
        }
    }

    public static String getStringRes(@StringRes int stringId) {
        return App.context.getResources().getString(stringId);
    }


    /**
     * Agreement exception This specific rule needs to be discussed and defined with the server or leader
     */
    public static class ERROR {
        /**
         * unknown mistake
         */
        public static final int UNKNOWN = 1001000;
        /**
         * parsing error
         */
        public static final int PARSE_ERROR = 1001001;
        /**
         * network error
         */
        public static final int NETWORD_ERROR = 1001002;
        /**
         * protocol error
         */
        public static final int HTTP_ERROR = 1001003;

        /**
         * certificate error
         */
        public static final int SSL_ERROR = 1001005;

        /**
         * connection timed out
         */
        public static final int TIMEOUT_ERROR = 1001006;
    }


}

