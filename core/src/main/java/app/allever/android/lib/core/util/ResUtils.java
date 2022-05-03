/**
 * Copyright (C) 2014-2017 <a href="http://www.sikefeng.com>">sikefeng</a> All Rights Reserved.
 */
package app.allever.android.lib.core.util;

import android.graphics.drawable.Drawable;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;


import app.allever.android.lib.core.app.App;


public class ResUtils {

    /**
     * 功能描述：根据ID获取颜色
     * @param colorId 颜色ID
     * @return 颜色的INT
     */
    public static int getColor(@ColorRes int colorId){
        return ContextCompat.getColor(App.context, colorId);

    }

    /**
     * 根据字符串ID获取字符
     * @param strId 字符串ID
     * @return 字符串名称
     */
    public static String getString(@StringRes int strId){
        return App.context.getString(strId);
    }

    /**
     * 根据integerId获取int
     * @param integerId integerId
     * @return int
     */
    public static int getInteger(@IntegerRes int integerId){
        return App.context.getResources().getInteger(integerId);
    }

    /**
     * 根据数组ID获取数组
     * @param arrId 数组ID
     * @return String[]
     */
    public static String[] getArrStr(int arrId){
        return App.context.getResources().getStringArray(arrId);
    }

    /**
     * 获取整型数组
     * @param arrId 数组ID
     * @return int[]
     */
    public static int[] getArrInt(int arrId){
        return App.context.getResources().getIntArray(arrId);
    }

    /**
     * 获取文件资源
     * @param imgId 资源ID
     * @return Drawable
     */
    public static Drawable getDrawable(@DrawableRes int imgId){
        return ContextCompat.getDrawable(App.context, imgId);
    }

    /**
     * 获取Dimen单位值
     * @param dimenId dimenId
     * @return float数值
     */
    public static float getDimen(@DimenRes int dimenId){
        return App.context.getResources().getDimension(dimenId);
    }
}
