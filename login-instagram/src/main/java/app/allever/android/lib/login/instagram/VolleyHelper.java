package app.allever.android.lib.login.instagram;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * @author allever
 */
public class VolleyHelper {

    private RequestQueue mQueue;

    public void init(Context context) {
        mQueue = Volley.newRequestQueue(context);
    }

    private VolleyHelper() {
    }
    private static class Holder {
        private static final VolleyHelper INS = new VolleyHelper();
    }
    public static VolleyHelper getInstance() {
        return Holder.INS;
    }
}
