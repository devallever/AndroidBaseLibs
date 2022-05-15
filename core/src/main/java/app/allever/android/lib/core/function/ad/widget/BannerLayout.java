package app.allever.android.lib.core.function.ad.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import app.allever.android.lib.core.R;

/**
 * @author allever
 */
public class BannerLayout extends RelativeLayout {

    private ViewGroup mBannerContainer;

    public BannerLayout(@NonNull Context context) {
        this(context, null);
    }

    public BannerLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.ad_banner_layout, this);
        mBannerContainer = root.findViewById(R.id.adLibBannerContainer);
    }

    public ViewGroup getBannerContainer() {
        return mBannerContainer;
    }
}
