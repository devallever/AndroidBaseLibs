package app.allever.android.lib.network

import android.net.ParseException
import androidx.annotation.StringRes
import app.allever.android.lib.core.app.App
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * Created by goldze on 2017/5/11.
 */
object ExceptionHandle {
    private const val FORBIDDEN = 400
    private const val NEXT_FOUND = 201
    private const val NOT_FOUND = 404
    private const val INTERNAL_SERVER_ERROR = 500
    private const val SERVICE_UNAVAILABLE = 503
    fun handleException(e: Throwable?): ResultThrowable {
        val ex: ResultThrowable
        return if (e is HttpException) {
            ex = ResultThrowable(e, ERROR.HTTP_ERROR)
            when (e.response()?.getCode()) {
                FORBIDDEN -> ex.message = getStringRes(R.string.request_failed)
                NEXT_FOUND -> ex.message = getStringRes(R.string.next_action_prompt)
                NOT_FOUND -> ex.message = getStringRes(R.string.interface_does_not_exist)
                INTERNAL_SERVER_ERROR -> ex.message = getStringRes(R.string.network_unavailable)
                SERVICE_UNAVAILABLE -> ex.message = getStringRes(R.string.server_unavailable)
                else -> ex.message = getStringRes(R.string.network_error)
            }
            ex
        } else if (e is JsonParseException
            || e is JSONException
            || e is ParseException
            || e is MalformedJsonException
        ) {
            ex = ResultThrowable(e, ERROR.PARSE_ERROR)
            ex.message = getStringRes(R.string.parsing_error)
            ex
        } else if (e is ConnectException) {
            ex = ResultThrowable(e, ERROR.NETWORD_ERROR)
            ex.message = getStringRes(R.string.connection_failed)
            ex
        } else if (e is SSLException) {
            ex = ResultThrowable(e, ERROR.SSL_ERROR)
            ex.message = getStringRes(R.string.certificate_verification_failed)
            ex
        } else if (e is ConnectTimeoutException || e is SocketTimeoutException) {
            ex = ResultThrowable(e, ERROR.TIMEOUT_ERROR)
            ex.message = getStringRes(R.string.connection_timed_out)
            ex
        } else if (e is UnknownHostException) {
            ex = try {
                ResultThrowable(e, ERROR.TIMEOUT_ERROR)
            } catch (es: Exception) {
                ResultThrowable(es, ERROR.TIMEOUT_ERROR)
            }
            ex.message = getStringRes(R.string.host_address_unknown)
            ex
        } else {
            ex = ResultThrowable(e, ERROR.UNKNOWN)
            ex.message = getStringRes(R.string.unknown_mistake)
            ex
        }
    }

    fun getStringRes(@StringRes stringId: Int): String {
        return App.context.resources.getString(stringId)
    }

    /**
     * Agreement exception This specific rule needs to be discussed and defined with the server or leader
     */
    object ERROR {
        /**
         * unknown mistake
         */
        const val UNKNOWN = 1001000

        /**
         * parsing error
         */
        const val PARSE_ERROR = 1001001

        /**
         * network error
         */
        const val NETWORD_ERROR = 1001002

        /**
         * protocol error
         */
        const val HTTP_ERROR = 1001003

        /**
         * certificate error
         */
        const val SSL_ERROR = 1001005

        /**
         * connection timed out
         */
        const val TIMEOUT_ERROR = 1001006
    }
}