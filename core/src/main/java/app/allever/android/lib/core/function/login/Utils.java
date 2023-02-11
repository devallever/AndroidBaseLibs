package app.allever.android.lib.core.function.login;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author allever
 */
public class Utils {
    private Utils() {}


    /**
     * 用于判断应用是否安装
     *
     * @param context
     * @param pkgName
     * @return
     */
    public static boolean isInstallApp(Context context, String pkgName) {
        if (TextUtils.isEmpty(pkgName)) {
            return false;
        }

        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
            if (packageInfo != null) {
                return true;
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * Bitmap to bytes.
     *
     * @param bitmap The bitmap.
     * @return bytes
     */
    public static byte[] bitmap2Bytes(final Bitmap bitmap) {
        return bitmap2Bytes(bitmap, Bitmap.CompressFormat.PNG, 100);
    }

    /**
     * Bitmap to bytes.
     *
     * @param bitmap  The bitmap.
     * @param format  The format of bitmap.
     * @param quality The quality.
     * @return bytes
     */
    public static byte[] bitmap2Bytes(final Bitmap bitmap, final Bitmap.CompressFormat format, int quality) {
        if (bitmap == null) return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(format, quality, baos);
        return baos.toByteArray();
    }

    /**
     * Return the bytes in file by stream.
     *
     * @param file     The file.
     * @return the bytes in file
     */
    public static byte[] readFile2BytesByStream(Context context, final File file) {

        if (!isFileExists(context, file)) {
            return null;
        }
        try {
            int bufferSize = 1024;
            ByteArrayOutputStream os = null;
            InputStream is = new BufferedInputStream(new FileInputStream(file), bufferSize);
            try {
                os = new ByteArrayOutputStream();
                byte[] b = new byte[bufferSize];
                int len;
                while ((len = is.read(b, 0, bufferSize)) != -1) {
                    os.write(b, 0, len);
                }
                return os.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    if (os != null) {
                        os.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Return whether the file exists.
     *
     * @param file The file.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isFileExists(Context context, final File file) {
        if (file == null) {
            return false;
        }
        if (file.exists()) {
            return true;
        }
        return isFileExists(context, file.getAbsolutePath());
    }

    /**
     * Return whether the file exists.
     *
     * @param filePath The path of file.
     * @return {@code true}: yes<br>{@code false}: no
     */
    public static boolean isFileExists(Context context, final String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        }
        return isFileExistsApi29(context, filePath);
    }

    private static boolean isFileExistsApi29(Context context, String filePath) {
        if (Build.VERSION.SDK_INT >= 29) {
            try {
                Uri uri = Uri.parse(filePath);
                ContentResolver cr = context.getContentResolver();
                AssetFileDescriptor afd = cr.openAssetFileDescriptor(uri, "r");
                if (afd == null) {
                    return false;
                }
                try {
                    afd.close();
                } catch (IOException ignore) {
                }
            } catch (FileNotFoundException e) {
                return false;
            }
            return true;
        }
        return false;
    }

    public static boolean checkFile(Context context, String path) {
        if (TextUtils.isEmpty(path)) {
            toast(context, "文件路径不能为空");
            return false;
        }

        File file = new File(path);
        if (!file.exists()) {
            toast(context, "文件不存在： " + path);
            return false;
        }

        if (!file.canRead()) {
            toast(context, "不能读取文件，请检查权限");
            return false;
        }

        return true;
    }

    private static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
