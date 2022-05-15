package app.allever.android.lib.core.util;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.TextAppearanceSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.ColorInt;

/**
 * crate by jerry
 * 富文本字符串设置工具类
 */
public class SpanUtils {

    private static SpanUtils SpanManager;
    private Builder builder;

    private SpanUtils() {
    }

    public static synchronized SpanUtils getInstance(String text) {
        if (null == SpanManager) {
            SpanManager = new SpanUtils();
        }
        SpanManager.setText(text);
        return SpanManager;
    }

    /**
     * 设置文本并创建SpannableString对象
     */
    public void setText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (null == builder || (null != builder
                && !builder.spannable.equals(text))) {
            builder = new Builder(text);
        }
    }

    /**
     * 设置文本的部分文字前景色，也就是对文字上色，
     *
     * @param colorId 颜色的ID值
     *                颜色设置为GREEN，start为4，end为7，应该是“陈奕迅”三个字显示为绿色
     */
    public SpanUtils setForegroundColorSpan(int start, int end, @ColorInt int colorId) {
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(colorId);
        builder.spannable.setSpan(foregroundColorSpan, start
                , end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    public SpanUtils setImageSpan(Drawable drawable, int start, int end) {
        CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(drawable);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        builder.spannable.setSpan(imageSpan, start, end, ImageSpan.ALIGN_BASELINE);
        return this;
    }

    /**
     * 设置文本的部分文字背景颜色，
     */
    public SpanUtils setBackgroundColorSpan(int start, int end, @ColorInt int colorId) {
        BackgroundColorSpan backgroundColorSpan = new BackgroundColorSpan(colorId);
        builder.spannable.setSpan(backgroundColorSpan, start
                , end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置文本的部分文字的点击事件
     */
    public SpanUtils setClickableSpan(int start, int end, TextView mTextView
            , final OnTextClickedListener onTextClickedListener) {
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                ((TextView) widget).setText(((TextView) widget).getText());
                if (null != onTextClickedListener) {
                    onTextClickedListener.onTextClicked(widget);
                }
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        };
        builder.spannable.setSpan(clickableSpan, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        mTextView.setMovementMethod(LinkMovementMethod.getInstance());
        return this;
    }

    /**
     * EmbossMaskFilter实现浮雕效果
     * EmbossMaskFilter(float[] direction, float ambient, float specular, float blurRadius)
     * direction：float数组，定义长度为3的数组标量[x,y,z]，来指定光源的方向
     * ambient：环境光亮度，0~1
     * specular：镜面反射系数
     * blurRadius：模糊半径，必须>0
     * new EmbossMaskFilter(new float[]{10, 10, 10}, 0.5f, 1, 1)
     */
    public SpanUtils setEmbossMaskFilter(int start, int end, float[] direction, float ambient, float specular, float blurRadius) {
        MaskFilterSpan embossMaskFilterSpan =
                new MaskFilterSpan(new EmbossMaskFilter(direction, ambient, specular, blurRadius));
        builder.spannable.setSpan(embossMaskFilterSpan, start, end
                , Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        return this;
    }

    /**
     * BlurMaskFilter实现模糊效果
     * radius：模糊半径
     * style：有四个参数可选
     * BlurMaskFilter.Blur.NORMAL：内外模糊
     * BlurMaskFilter.Blur.OUTER：外部模糊
     * BlurMaskFilter.Blur.INNER：内部模糊
     * BlurMaskFilter.Blur.SOLID：内部加粗，外部模糊
     */
    public SpanUtils setBlurMaskFilter(int start, int end, int radius) {
        MaskFilterSpan blurMaskFilterSpan
                = new MaskFilterSpan(new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL));
        builder.spannable.setSpan(blurMaskFilterSpan, start, end
                , Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置字体的相对大小，这里设置为TextView大小的1.5倍，看图
     */
    public SpanUtils setRelativeSizeSpan(int start, int end, float proportion) {
        RelativeSizeSpan relativeSizeSpan = new RelativeSizeSpan(proportion);
        builder.spannable.setSpan(relativeSizeSpan, start, end
                , Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置字体的相绝对大小，40表示文字大小，true表示单位为dip，若为false
     */
    public SpanUtils setAbsoluteSizeSpan(int start, int end, int size, boolean isDip) {
        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(size, isDip);
        builder.spannable.setSpan(absoluteSizeSpan, start
                , end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置字体x轴缩放，1.5表示x轴放大为1.5倍，效果如图
     */
    public SpanUtils setScaleXSpan(int start, int end, float proportion) {
        ScaleXSpan scaleXSpan = new ScaleXSpan(proportion);
        builder.spannable.setSpan(scaleXSpan, start
                , end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置文字样式，如斜体、粗体
     * Typeface.BOLD
     * Typeface.ITALIC
     * Typeface.BOLD_ITALIC
     * Typeface.NORMAL
     * Typeface.STYLE_MASK
     */
    public SpanUtils setStyleSpan(int start, int end, int textTypeFace) {
        StyleSpan boldSpan = new StyleSpan(textTypeFace);
        builder.spannable.setSpan(boldSpan, start
                , end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置文字字体类型，如monospace、serif和sans-serif等等
     */
    public SpanUtils setTypefaceSpan(int start, int end, String family) {
        TypefaceSpan monospace = new TypefaceSpan(family);
        builder.spannable.setSpan(monospace, start
                , end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置文字外貌，通过style资源设置，这里使用系统的style资源
     * android.R.style.TextAppearance_Material
     */
    public SpanUtils setTextAppearanceSpan(int start, int end, Context mContext, int styleId) {
        TextAppearanceSpan textAppearanceSpan = new TextAppearanceSpan(mContext, styleId);
        builder.spannable.setSpan(textAppearanceSpan, start
                , end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置文字下划线，强调突出文字时可以使用该span
     */
    public SpanUtils setUnderlineSpan(int start, int end) {
        UnderlineSpan underlineSpan = new UnderlineSpan();
        builder.spannable.setSpan(underlineSpan, start
                , end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 设置文字删除线
     */
    public SpanUtils setStrikeThroughSpan(int start, int end) {
        StrikethroughSpan strikethroughSpan = new StrikethroughSpan();
        builder.spannable.setSpan(strikethroughSpan, start
                , end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        return this;
    }

    /**
     * 返回设置好的SpannableString对象
     */
    public SpannableString toBuild() {
        if (null == builder) {
            throw new NullPointerException("Uninitialized SpannableString");
        }
        return builder.spannable;
    }

    public interface OnTextClickedListener {
        void onTextClicked(View view);
    }

    private static class Builder {
        public SpannableString spannable;

        public Builder(String text) {
            this.spannable = new SpannableString(text);
        }
    }

    private static class CenterAlignImageSpan extends ImageSpan {

        public CenterAlignImageSpan(Drawable drawable) {
            super(drawable);

        }

        public int getSize(Paint paint, CharSequence text, int start, int end,
                           Paint.FontMetricsInt fontMetricsInt) {
            Drawable drawable = getDrawable();
            Rect rect = drawable.getBounds();
            if (fontMetricsInt != null) {
                Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
                int fontHeight = fmPaint.bottom - fmPaint.top;
                int drHeight = rect.bottom - rect.top;

                //对于这里我表示,我不知道为啥是这样。不应该是fontHeight/2?但是只有fontHeight/4才能对齐
                //难道是因为TextView的draw的时候top和bottom是大于实际的？具体请看下图
                //所以fontHeight/4是去除偏差?
                int top = drHeight / 2 - fontHeight / 4;
                int bottom = drHeight / 2 + fontHeight / 4;

                fontMetricsInt.ascent = -bottom;
                fontMetricsInt.top = -bottom;
                fontMetricsInt.bottom = top;
                fontMetricsInt.descent = top;
            }
            return rect.right;
        }

        @Override
        public void draw(Canvas canvas, CharSequence text, int start, int end,
                         float x, int top, int y, int bottom, Paint paint) {
            Drawable drawable = getDrawable();
            canvas.save();
            int transY = 0;
            //获得将要显示的文本高度-图片高度除2等居中位置+top(换行情况)
            transY = ((bottom - top) - drawable.getBounds().bottom) / 2 + top;
            canvas.translate(x, transY);
            drawable.draw(canvas);
            canvas.restore();
        }
    }
}
