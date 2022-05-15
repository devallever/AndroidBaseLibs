package app.allever.android.lib.core.util;


import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

public class StatusBarUtil {

    public static final int DEFAULT_STATUS_BAR_ALPHA = 112;

    /**
     * 设置状态栏颜色
     *
     * @param AppCompatActivity 需要设置的 AppCompatActivity
     * @param color             状态栏颜色值
     */
    public static void setColor(AppCompatActivity AppCompatActivity, @ColorInt int color) {
        setColor(AppCompatActivity, color, DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * 设置状态栏颜色
     *
     * @param AppCompatActivity 需要设置的AppCompatActivity
     * @param color             状态栏颜色值
     * @param statusBarAlpha    状态栏透明度
     */

    public static void setColor(AppCompatActivity AppCompatActivity, @ColorInt int color, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppCompatActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            AppCompatActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            AppCompatActivity.getWindow().setStatusBarColor(calculateStatusColor(color, statusBarAlpha));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppCompatActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            ViewGroup decorView = (ViewGroup) AppCompatActivity.getWindow().getDecorView();
            int count = decorView.getChildCount();
            if (count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
                decorView.getChildAt(count - 1).setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
            } else {
                StatusBarView statusView = createStatusBarView(AppCompatActivity, color, statusBarAlpha);
                decorView.addView(statusView);
            }
            setRootView(AppCompatActivity);
        }
    }

    /**
     * 为滑动返回界面设置状态栏颜色
     *
     * @param AppCompatActivity 需要设置的AppCompatActivity
     * @param color             状态栏颜色值
     */
    public static void setColorForSwipeBack(AppCompatActivity AppCompatActivity, int color) {
        setColorForSwipeBack(AppCompatActivity, color, DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * 为滑动返回界面设置状态栏颜色
     *
     * @param AppCompatActivity 需要设置的AppCompatActivity
     * @param color             状态栏颜色值
     * @param statusBarAlpha    状态栏透明度
     */
    public static void setColorForSwipeBack(AppCompatActivity AppCompatActivity, @ColorInt int color, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            ViewGroup contentView = ((ViewGroup) AppCompatActivity.findViewById(android.R.id.content));
            contentView.setPadding(0, getStatusBarHeight(AppCompatActivity), 0, 0);
            contentView.setBackgroundColor(calculateStatusColor(color, statusBarAlpha));
            setTransparentForWindow(AppCompatActivity);
        }
    }

    /**
     * 设置状态栏纯色 不加半透明效果
     *
     * @param AppCompatActivity 需要设置的 AppCompatActivity
     * @param color             状态栏颜色值
     */
    public static void setColorNoTranslucent(AppCompatActivity AppCompatActivity, @ColorInt int color) {
        setColor(AppCompatActivity, color, 0);
    }

    /**
     * 设置状态栏颜色(5.0以下无半透明效果,不建议使用)
     *
     * @param AppCompatActivity 需要设置的 AppCompatActivity
     * @param color             状态栏颜色值
     */
    @Deprecated
    public static void setColorDiff(AppCompatActivity AppCompatActivity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        transparentStatusBar(AppCompatActivity);
        ViewGroup contentView = (ViewGroup) AppCompatActivity.findViewById(android.R.id.content);
        // 移除半透明矩形,以免叠加
        if (contentView.getChildCount() > 1) {
            contentView.getChildAt(1).setBackgroundColor(color);
        } else {
            contentView.addView(createStatusBarView(AppCompatActivity, color));
        }
        setRootView(AppCompatActivity);
    }

    /**
     * 使状态栏半透明
     * <p>
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param AppCompatActivity 需要设置的AppCompatActivity
     */
    public static void setTranslucent(AppCompatActivity AppCompatActivity) {
        setTranslucent(AppCompatActivity, DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * 使状态栏半透明
     * <p>
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param AppCompatActivity 需要设置的AppCompatActivity
     * @param statusBarAlpha    状态栏透明度
     */
    public static void setTranslucent(AppCompatActivity AppCompatActivity, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setTransparent(AppCompatActivity);
        addTranslucentView(AppCompatActivity, statusBarAlpha);
    }

    /**
     * 针对根布局是 CoordinatorLayout, 使状态栏半透明
     * <p>
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param AppCompatActivity 需要设置的AppCompatActivity
     * @param statusBarAlpha    状态栏透明度
     */
    public static void setTranslucentForCoordinatorLayout(AppCompatActivity AppCompatActivity, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        transparentStatusBar(AppCompatActivity);
        addTranslucentView(AppCompatActivity, statusBarAlpha);
    }

    /**
     * 设置状态栏全透明
     *
     * @param AppCompatActivity 需要设置的AppCompatActivity
     */
    public static void setTransparent(AppCompatActivity AppCompatActivity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        transparentStatusBar(AppCompatActivity);
        setRootView(AppCompatActivity);
    }

    /**
     * 使状态栏透明(5.0以上半透明效果,不建议使用)
     * <p>
     * 适用于图片作为背景的界面,此时需要图片填充到状态栏
     *
     * @param AppCompatActivity 需要设置的AppCompatActivity
     */
    @Deprecated
    public static void setTranslucentDiff(AppCompatActivity AppCompatActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            AppCompatActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            setRootView(AppCompatActivity);
        }
    }

    /**
     * 为DrawerLayout 布局设置状态栏变色
     *
     * @param AppCompatActivity 需要设置的AppCompatActivity
     * @param drawerLayout      DrawerLayout
     * @param color             状态栏颜色值
     */
    public static void setColorForDrawerLayout(AppCompatActivity AppCompatActivity, DrawerLayout drawerLayout, @ColorInt int color) {
        setColorForDrawerLayout(AppCompatActivity, drawerLayout, color, DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * 为DrawerLayout 布局设置状态栏颜色,纯色
     *
     * @param AppCompatActivity 需要设置的AppCompatActivity
     * @param drawerLayout      DrawerLayout
     * @param color             状态栏颜色值
     */
    public static void setColorNoTranslucentForDrawerLayout(AppCompatActivity AppCompatActivity, DrawerLayout drawerLayout, @ColorInt int color) {
        setColorForDrawerLayout(AppCompatActivity, drawerLayout, color, 0);
    }

    /**
     * 为DrawerLayout 布局设置状态栏变色
     *
     * @param AppCompatActivity 需要设置的AppCompatActivity
     * @param drawerLayout      DrawerLayout
     * @param color             状态栏颜色值
     * @param statusBarAlpha    状态栏透明度
     */
    public static void setColorForDrawerLayout(AppCompatActivity AppCompatActivity, DrawerLayout drawerLayout, @ColorInt int color,
                                               int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppCompatActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            AppCompatActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            AppCompatActivity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            AppCompatActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        // 生成一个状态栏大小的矩形
        // 添加 statusBarView 到布局中
        ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
        if (contentLayout.getChildCount() > 0 && contentLayout.getChildAt(0) instanceof StatusBarView) {
            contentLayout.getChildAt(0).setBackgroundColor(color);
        } else {
            StatusBarView statusBarView = createStatusBarView(AppCompatActivity, color);
            contentLayout.addView(statusBarView, 0);
        }
        // 内容布局不是 LinearLayout 时,设置padding top
        if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1)
                    .setPadding(contentLayout.getPaddingLeft(), getStatusBarHeight(AppCompatActivity) + contentLayout.getPaddingTop(),
                            contentLayout.getPaddingRight(), contentLayout.getPaddingBottom());
        }
        // 设置属性
        setDrawerLayoutProperty(drawerLayout, contentLayout);
        addTranslucentView(AppCompatActivity, statusBarAlpha);
    }

    /**
     * 设置 DrawerLayout 属性
     *
     * @param drawerLayout              DrawerLayout
     * @param drawerLayoutContentLayout DrawerLayout 的内容布局
     */
    private static void setDrawerLayoutProperty(DrawerLayout drawerLayout, ViewGroup drawerLayoutContentLayout) {
        ViewGroup drawer = (ViewGroup) drawerLayout.getChildAt(1);
        drawerLayout.setFitsSystemWindows(false);
        drawerLayoutContentLayout.setFitsSystemWindows(false);
        drawerLayoutContentLayout.setClipToPadding(true);
        drawer.setFitsSystemWindows(false);
    }

    /**
     * 为DrawerLayout 布局设置状态栏变色(5.0以下无半透明效果,不建议使用)
     *
     * @param AppCompatActivity 需要设置的AppCompatActivity
     * @param drawerLayout      DrawerLayout
     * @param color             状态栏颜色值
     */
    @Deprecated
    public static void setColorForDrawerLayoutDiff(AppCompatActivity AppCompatActivity, DrawerLayout drawerLayout, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppCompatActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            if (contentLayout.getChildCount() > 0 && contentLayout.getChildAt(0) instanceof StatusBarView) {
                contentLayout.getChildAt(0).setBackgroundColor(calculateStatusColor(color, DEFAULT_STATUS_BAR_ALPHA));
            } else {
                // 添加 statusBarView 到布局中
                StatusBarView statusBarView = createStatusBarView(AppCompatActivity, color);
                contentLayout.addView(statusBarView, 0);
            }
            // 内容布局不是 LinearLayout 时,设置padding top
            if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
                contentLayout.getChildAt(1).setPadding(0, getStatusBarHeight(AppCompatActivity), 0, 0);
            }
            // 设置属性
            setDrawerLayoutProperty(drawerLayout, contentLayout);
        }
    }

    /**
     * 为 DrawerLayout 布局设置状态栏透明
     *
     * @param AppCompatActivity 需要设置的AppCompatActivity
     * @param drawerLayout      DrawerLayout
     */
    public static void setTranslucentForDrawerLayout(AppCompatActivity AppCompatActivity, DrawerLayout drawerLayout) {
        setTranslucentForDrawerLayout(AppCompatActivity, drawerLayout, DEFAULT_STATUS_BAR_ALPHA);
    }

    /**
     * 为 DrawerLayout 布局设置状态栏透明
     *
     * @param AppCompatActivity 需要设置的AppCompatActivity
     * @param drawerLayout      DrawerLayout
     */
    public static void setTranslucentForDrawerLayout(AppCompatActivity AppCompatActivity, DrawerLayout drawerLayout, int statusBarAlpha) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setTransparentForDrawerLayout(AppCompatActivity, drawerLayout);
        addTranslucentView(AppCompatActivity, statusBarAlpha);
    }

    /**
     * 为 DrawerLayout 布局设置状态栏透明
     *
     * @param AppCompatActivity 需要设置的AppCompatActivity
     * @param drawerLayout      DrawerLayout
     */
    public static void setTransparentForDrawerLayout(AppCompatActivity AppCompatActivity, DrawerLayout drawerLayout) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppCompatActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            AppCompatActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            AppCompatActivity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            AppCompatActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
        // 内容布局不是 LinearLayout 时,设置padding top
        if (!(contentLayout instanceof LinearLayout) && contentLayout.getChildAt(1) != null) {
            contentLayout.getChildAt(1).setPadding(0, getStatusBarHeight(AppCompatActivity), 0, 0);
        }

        // 设置属性
        setDrawerLayoutProperty(drawerLayout, contentLayout);
    }

    /**
     * 为 DrawerLayout 布局设置状态栏透明(5.0以上半透明效果,不建议使用)
     *
     * @param AppCompatActivity 需要设置的AppCompatActivity
     * @param drawerLayout      DrawerLayout
     */
    @Deprecated
    public static void setTranslucentForDrawerLayoutDiff(AppCompatActivity AppCompatActivity, DrawerLayout drawerLayout) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            AppCompatActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 设置内容布局属性
            ViewGroup contentLayout = (ViewGroup) drawerLayout.getChildAt(0);
            contentLayout.setFitsSystemWindows(true);
            contentLayout.setClipToPadding(true);
            // 设置抽屉布局属性
            ViewGroup vg = (ViewGroup) drawerLayout.getChildAt(1);
            vg.setFitsSystemWindows(false);
            // 设置 DrawerLayout 属性
            drawerLayout.setFitsSystemWindows(false);
        }
    }

    /**
     * 为头部是 ImageView 的界面设置状态栏全透明
     *
     * @param AppCompatActivity 需要设置的AppCompatActivity
     * @param needOffsetView    需要向下偏移的 View
     */
    public static void setTransparentForImageView(AppCompatActivity AppCompatActivity, View needOffsetView) {
        setTranslucentForImageView(AppCompatActivity, 0, needOffsetView);
    }

    /**
     * 为头部是 ImageView 的界面设置状态栏透明(使用默认透明度)
     *
     * @param AppCompatActivity 需要设置的AppCompatActivity
     * @param needOffsetView    需要向下偏移的 View
     */
    public static void setTranslucentForImageView(AppCompatActivity AppCompatActivity, View needOffsetView) {
        setTranslucentForImageView(AppCompatActivity, DEFAULT_STATUS_BAR_ALPHA, needOffsetView);
    }

    /**
     * 为头部是 ImageView 的界面设置状态栏透明
     *
     * @param AppCompatActivity 需要设置的AppCompatActivity
     * @param statusBarAlpha    状态栏透明度
     * @param needOffsetView    需要向下偏移的 View
     */
    public static void setTranslucentForImageView(AppCompatActivity AppCompatActivity, int statusBarAlpha, View needOffsetView) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        setTransparentForWindow(AppCompatActivity);
        addTranslucentView(AppCompatActivity, statusBarAlpha);
        if (needOffsetView != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) needOffsetView.getLayoutParams();
            layoutParams.setMargins(0, getStatusBarHeight(AppCompatActivity), 0, 0);
        }
    }

    /**
     * 为 fragment 头部是 ImageView 的设置状态栏透明
     *
     * @param AppCompatActivity fragment 对应的 AppCompatActivity
     * @param needOffsetView    需要向下偏移的 View
     */
    public static void setTranslucentForImageViewInFragment(AppCompatActivity AppCompatActivity, View needOffsetView) {
        setTranslucentForImageViewInFragment(AppCompatActivity, DEFAULT_STATUS_BAR_ALPHA, needOffsetView);
    }

    /**
     * 为 fragment 头部是 ImageView 的设置状态栏透明
     *
     * @param AppCompatActivity fragment 对应的 AppCompatActivity
     * @param needOffsetView    需要向下偏移的 View
     */
    public static void setTransparentForImageViewInFragment(AppCompatActivity AppCompatActivity, View needOffsetView) {
        setTranslucentForImageViewInFragment(AppCompatActivity, 0, needOffsetView);
    }

    /**
     * 为 fragment 头部是 ImageView 的设置状态栏透明
     *
     * @param AppCompatActivity fragment 对应的 AppCompatActivity
     * @param statusBarAlpha    状态栏透明度
     * @param needOffsetView    需要向下偏移的 View
     */
    public static void setTranslucentForImageViewInFragment(AppCompatActivity AppCompatActivity, int statusBarAlpha, View needOffsetView) {
        setTranslucentForImageView(AppCompatActivity, statusBarAlpha, needOffsetView);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            clearPreviousSetting(AppCompatActivity);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void clearPreviousSetting(AppCompatActivity AppCompatActivity) {
        ViewGroup decorView = (ViewGroup) AppCompatActivity.getWindow().getDecorView();
        int count = decorView.getChildCount();
        if (count > 0 && decorView.getChildAt(count - 1) instanceof StatusBarView) {
            decorView.removeViewAt(count - 1);
            ViewGroup rootView = (ViewGroup) ((ViewGroup) AppCompatActivity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setPadding(0, 0, 0, 0);
        }
    }

    /**
     * 添加半透明矩形条
     *
     * @param AppCompatActivity 需要设置的 AppCompatActivity
     * @param statusBarAlpha    透明值
     */
    private static void addTranslucentView(AppCompatActivity AppCompatActivity, int statusBarAlpha) {
        ViewGroup contentView = (ViewGroup) AppCompatActivity.findViewById(android.R.id.content);
        if (contentView.getChildCount() > 1) {
            contentView.getChildAt(1).setBackgroundColor(Color.argb(statusBarAlpha, 0, 0, 0));
        } else {
            contentView.addView(createTranslucentStatusBarView(AppCompatActivity, statusBarAlpha));
        }
    }

    /**
     * 生成一个和状态栏大小相同的彩色矩形条
     *
     * @param AppCompatActivity 需要设置的 AppCompatActivity
     * @param color             状态栏颜色值
     * @return 状态栏矩形条
     */
    private static StatusBarView createStatusBarView(AppCompatActivity AppCompatActivity, @ColorInt int color) {
        // 绘制一个和状态栏一样高的矩形
        StatusBarView statusBarView = new StatusBarView(AppCompatActivity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(AppCompatActivity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(color);
        return statusBarView;
    }

    /**
     * 生成一个和状态栏大小相同的半透明矩形条
     *
     * @param AppCompatActivity 需要设置的AppCompatActivity
     * @param color             状态栏颜色值
     * @param alpha             透明值
     * @return 状态栏矩形条
     */
    private static StatusBarView createStatusBarView(AppCompatActivity AppCompatActivity, @ColorInt int color, int alpha) {
        // 绘制一个和状态栏一样高的矩形
        StatusBarView statusBarView = new StatusBarView(AppCompatActivity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(AppCompatActivity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(calculateStatusColor(color, alpha));
        return statusBarView;
    }

    /**
     * 设置根布局参数
     */
    private static void setRootView(AppCompatActivity AppCompatActivity) {
        ViewGroup parent = (ViewGroup) AppCompatActivity.findViewById(android.R.id.content);
        for (int i = 0, count = parent.getChildCount(); i < count; i++) {
            View childView = parent.getChildAt(i);
            if (childView instanceof ViewGroup) {
                childView.setFitsSystemWindows(true);
                ((ViewGroup) childView).setClipToPadding(true);
            }
        }
    }

    /**
     * 设置透明
     */
    private static void setTransparentForWindow(AppCompatActivity AppCompatActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppCompatActivity.getWindow().setStatusBarColor(Color.TRANSPARENT);
            AppCompatActivity.getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppCompatActivity.getWindow()
                    .setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 使状态栏透明
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private static void transparentStatusBar(AppCompatActivity AppCompatActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AppCompatActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            AppCompatActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            AppCompatActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            AppCompatActivity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else {
            AppCompatActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 创建半透明矩形 View
     *
     * @param alpha 透明值
     * @return 半透明 View
     */
    private static StatusBarView createTranslucentStatusBarView(AppCompatActivity AppCompatActivity, int alpha) {
        // 绘制一个和状态栏一样高的矩形
        StatusBarView statusBarView = new StatusBarView(AppCompatActivity);
        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(AppCompatActivity));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
        return statusBarView;
    }

    /**
     * 获取状态栏高度
     *
     * @param context context
     * @return 状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        // 获得状态栏高度
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    /**
     * 计算状态栏颜色
     *
     * @param color color值
     * @param alpha alpha值
     * @return 最终的状态栏颜色
     */
    private static int calculateStatusColor(@ColorInt int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    /**
     * Created by Jaeger on 16/6/8.
     * <p>
     * Email: chjie.jaeger@gmail.com
     * GitHub: https://github.com/laobie
     */
    public static class StatusBarView extends View {
        public StatusBarView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public StatusBarView(Context context) {
            super(context);
        }
    }

}
