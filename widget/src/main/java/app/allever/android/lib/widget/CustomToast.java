package app.allever.android.lib.widget;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import app.allever.android.lib.core.app.App;


/**
 * @author Administrator
 * CustomToast.builder()
 * .showImage(true)
 * .showText(true)
 * .onlyDebug(true) //默认全弹
 * .text("展示文本和图片就设置true，默认不展示")
 * .icon(R.drawable.tianliaologo)
 * .show();
 */
public class CustomToast {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean showText = true;
        private boolean showImage = false;
        private String msg = "";
        private boolean onlyDebug = false;
        private int icon = 0;
        private int layoutId = R.layout.custom_toast_layout;
        private int duration = Toast.LENGTH_SHORT;

        public Builder showText(boolean show) {
            this.showText = show;
            return this;
        }

        public Builder showImage(boolean show) {
            this.showImage = show;
            return this;
        }

        public Builder text(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder icon(int resId) {
            this.icon = resId;
            return this;
        }

        public Builder layout(int layoutId) {
            this.layoutId = layoutId;
            return this;
        }

        public Builder onlyDebug(boolean onlyDebug) {
            this.onlyDebug = onlyDebug;
            return this;
        }

        public Builder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public void show() {
            if (TextUtils.isEmpty(msg) && showText) {
                return;
            }
            View toastLayout = LayoutInflater.from(App.context).inflate(layoutId, null);
            TextView textView = (TextView) toastLayout.findViewById(R.id.tvMsg);
            ImageView imageView = toastLayout.findViewById(R.id.ivIcon);
            if (showText) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(msg);
            } else {
                textView.setVisibility(View.GONE);
            }

            if (showImage) {
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(icon);
            } else {
                imageView.setVisibility(View.GONE);
            }

            if (showText && !showImage) {
                //只显示文本到时候讲上边距设为0， 确保居中显示
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
                layoutParams.topMargin = 0;
                textView.setLayoutParams(layoutParams);
            }

            Toast toast = new Toast(App.context);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.setDuration(duration);
            toast.setView(toastLayout);
            if (onlyDebug) {
                if (BuildConfig.DEBUG) {
                    toast.show();
                }
            } else {
                toast.show();
            }
        }
    }

}
