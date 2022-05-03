package app.allever.android.lib.core.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * Created by Juzhengyuan
 *
 * @Author: Jerry.
 * @Date: 2020/11/25 16
 * @Desc:
 */
public class BlurBgUtils {

    private static BlurBgUtils utils;
    private View mRoot;
    private MutableLiveData<Bitmap> mBitMapData = new MutableLiveData<>();

    public static BlurBgUtils getUtils(){
        if (null == utils) {
            utils = new BlurBgUtils();
        }
        return utils;
    }

    /**
     * 设置页面需要高斯模糊的view
     * @param view
     */
    public void setRootBg(View view){
        mRoot = view;
    }

    /**
     * 把view转为bitmap
     * @param view
     * @return
     */
    private Bitmap createBitmapFromView(View view) {
        //是ImageView直接获取
        if (view instanceof ImageView) {
            Drawable drawable = ((ImageView) view).getDrawable();
            if (drawable instanceof BitmapDrawable) {
                return ((BitmapDrawable) drawable).getBitmap();
            }
        }
        view.clearFocus();
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        if (bitmap != null) {
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            canvas.setBitmap(null);
        }
        return bitmap;
    }

    /**
     * 开始创建bitmap并保存到bitmap
     */
    public void createImageBg(){
        mBitMapData.setValue(createBitmapFromView(mRoot));
    }

    /**
     * 注册数据回调
     * @param mOwner
     * @param observer
     */
    public void register(LifecycleOwner mOwner, Observer<Bitmap> observer){
        mBitMapData.observe(mOwner,observer);
    }

    /**
     * 清除view
     */
    public void clearView(){
        mRoot = null;
    }

    /**
     * 清除bitmap
     */
    public void clear(){
        Bitmap value = mBitMapData.getValue();
        value.recycle();
        mBitMapData.setValue(null);
    }
}
