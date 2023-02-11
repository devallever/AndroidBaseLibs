package app.allever.android.lib.core.function.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author allever
 */
public class ResultUtils {

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;
    private static final int RESULT_SUCCESS_CODE = 0;
    private static final int RESULT_FAILURE_CODE = 1;

    private ResultUtils() {}

    public static void init(Context context) {
        if (sContext == null) {
            sContext = context.getApplicationContext();
        }
    }

    public static JSONObject createSuccessResult(Object msg) {
        return createResultJsonObject(RESULT_SUCCESS_CODE, msg);
    }

    public static JSONObject createFailureResult(String msg) {
        Toast.makeText(sContext, msg, Toast.LENGTH_SHORT).show();
        return createResultJsonObject(RESULT_FAILURE_CODE, msg);
    }

    public static JSONObject createResultJsonObject(int code, Object obj) {
        JSONObject object = new JSONObject();
        try {
            object.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            object.put("data", obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

}
