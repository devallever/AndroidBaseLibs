package app.allever.android.lib.core.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import app.allever.android.lib.core.ext.ToastKt;

/**
 * 放一些无法分类到方法
 */
public class CommonUtils {

    public static void openAppInPlay(Context context, String uristring, String appstore) {
        if (!uristring.startsWith("market") && !uristring.startsWith("http")) {
            uristring = "market://details?id=" + uristring;
        }
        Uri uri = Uri.parse(uristring);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (!TextUtils.isEmpty(appstore)) {
            if (isThereApp(context, appstore)) {
                intent.setPackage(appstore);
            }
        }

        try {
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            ToastKt.toast("没有安装谷歌服务框架");
        }
    }


    public static void openInGooglePlay(Context context, String uriString) {
        openAppInPlay(context, uriString, "com.android.vending");
    }

    public static void openAppInPlay(Context context, String uristring) {
        openAppInPlay(context, uristring, null);
    }

    public static boolean isThereApp(Context context, String pkg) {
        try {
            if (pkg.startsWith("market")) {
                pkg = pkg.replaceAll(".*id=", "");
            }
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(pkg, PackageManager.GET_META_DATA);
            if (null != packageInfo) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getPackeName(String pkg) {
        String str = pkg.replace(".", "");
        return str;
    }

}
