package app.allever.android.lib.core.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;

/**
 * create by jerry
 * 图片压缩工具
 */
public class CompressImageUtils {

    private Context mContent;
    private OnImageCallBack onImageCallBack;
    private static CompressImageUtils compressImageManager;
    private static WeakReference<Context> contextWeakReference;
    private int quality = 100;
    private int width = 0;
    private int height = 0;
    private String TAG = CompressImageUtils.class.getSimpleName();

    public static CompressImageUtils getInstance(Context mContent, OnImageCallBack onImageCallBack) {
        if (null == compressImageManager) {
            compressImageManager = new CompressImageUtils();
        }
        contextWeakReference = new WeakReference<>(mContent);
        compressImageManager.setContextAndOnImageCallBack(contextWeakReference.get(), onImageCallBack);
        return compressImageManager;
    }

    private CompressImageUtils() {
    }

    /**
     * 设置初始化信息
     *
     * @param mContent
     * @param onImageCallBack 完成回调
     */
    public void setContextAndOnImageCallBack(Context mContent, OnImageCallBack onImageCallBack) {
        this.mContent = mContent;
        this.onImageCallBack = onImageCallBack;
    }

    /**
     * 设置图片质量百分比
     */
    public CompressImageUtils setQuality(int quality) {
        this.quality = quality;
        return this;
    }

    /**
     * 设置压缩的图片尺寸
     */
    public CompressImageUtils setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    /**
     * 压缩图片使用,采用BitmapFactory.decodeFile。这里是尺寸压缩
     */
    public void bitmapFactory(String imagePath) {
        // 配置压缩的参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //获取当前图片的边界大小，而不是将整张图片载入在内存中，避免内存溢出
        BitmapFactory.decodeFile(imagePath, options);
        //如果没有设置压缩的长宽则使用图片原来的尺寸
        width = width != 0 ? width : options.outWidth == 0 ? 500 : options.outWidth;
        height = height != 0 ? height : options.outHeight == 0 ? 500 : options.outHeight;
        Log.e(TAG, "image width:" + width + ",height:" + height);
        options.inJustDecodeBounds = false;
        ////inSampleSize的作用就是可以把图片的长短缩小inSampleSize倍，所占内存缩小inSampleSize的平方
        options.inSampleSize = caculateSampleSize(options, width, height);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options); // 解码文件
        bitmapCompress(bitmap);
    }

    /**
     * 计算出所需要压缩的大小
     *
     * @param options
     * @param reqWidth  我们期望的图片的宽，单位px
     * @param reqHeight 我们期望的图片的高，单位px
     * @return
     */
    private int caculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int sampleSize = 1;
        int picWidth = options.outWidth;
        int picHeight = options.outHeight;
        if (picWidth > reqWidth || picHeight > reqHeight) {
            int halfPicWidth = picWidth / 2;
            int halfPicHeight = picHeight / 2;
            while (halfPicWidth / sampleSize > reqWidth || halfPicHeight / sampleSize > reqHeight) {
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }


    /**
     * 这里我们生成了一个Pic文件夹，在下面放了我们质量压缩后的图片，用于和原图对比
     * 压缩图片使用Bitmap.compress()，这里是质量压缩
     */
    private void bitmapCompress(Bitmap photoBitmap) {
        //创建路径
        String path = mContent.getExternalCacheDir().getAbsolutePath() + "/";
        //获取外部储存目录
        File file = new File(path);
        //创建新目录, 创建此抽象路径名指定的目录，包括创建必需但不存在的父目录。
        file.mkdirs();
        //以当前时间重新命名文件
        long i = System.currentTimeMillis();
        //生成新的文件
        file = new File(file.getAbsolutePath() + "/" + i + ".png");
        Log.e(TAG, file.getPath());
        //创建输出流
        OutputStream out = null;
        try {
            out = new FileOutputStream(file.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //压缩文件，返回结果，参数分别是压缩的格式，压缩质量的百分比，输出流
        photoBitmap.compress(Bitmap.CompressFormat.PNG, quality, out);
        if (null != onImageCallBack) {
            onImageCallBack.callBack(file);
        }
        photoBitmap.recycle();
    }

    public interface OnImageCallBack {
        void callBack(File file);
    }
}
