package app.allever.android.lib.core.util;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

import app.allever.android.lib.core.app.App;
import app.allever.android.lib.core.ext.ToastKt;
import app.allever.android.lib.core.helper.SPHelper;

public class AppUtils {

    private static final String SP_FIRST_LAUNCH = "SP_FIRST_LAUNCH";

    private AppUtils() {
    }

    /**
     * 安装代码块
     */
    public static void installAPK(File apkFile) {
        if (!FileUtils.isFileExists(apkFile)) {
            ToastKt.toast("文件不存在");
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (apkFile.getName().endsWith(".apk")) {

            try {
                //兼容7.0
                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    uri = FileProvider.getUriForFile(App.context, App.context.getPackageName() + ".fileprovider", apkFile);
                } else {
                    uri = Uri.fromFile(apkFile);
                }
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //弹出安装界面
        App.context.startActivity(intent);

    }

    /**
     * Install the app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param file The file.
     */
    public static void installApp(final File file) {
        Intent installAppIntent = getInstallAppIntent(file);
        if (installAppIntent == null) return;
        App.context.startActivity(installAppIntent);
    }

    /**
     * Return the intent of install app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param file The file.
     * @return the intent of install app
     */
    public static Intent getInstallAppIntent(final File file) {
        if (!FileUtils.isFileExists(file)) return null;
        Uri uri;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
        } else {
            String authority = App.context.getPackageName() + ".fileprovider";
            uri = FileProvider.getUriForFile(App.context, authority, file);
        }
        return getInstallAppIntent(uri);
    }

    /**
     * Return the intent of install app.
     * <p>Target APIs greater than 25 must hold
     * {@code <uses-permission android:name="android.permission.REQUEST_INSTALL_PACKAGES" />}</p>
     *
     * @param uri The uri.
     * @return the intent of install app
     */
    public static Intent getInstallAppIntent(final Uri uri) {
        if (uri == null) return null;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type = "application/vnd.android.package-archive";
        intent.setDataAndType(uri, type);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public static void handleFirstLaunch() {
        boolean isFirstLaunch = SPHelper.INSTANCE.getBoolean(SP_FIRST_LAUNCH, true);
        if (isFirstLaunch) {
            SPHelper.INSTANCE.putBoolean(SP_FIRST_LAUNCH, false);
        }
    }
}
