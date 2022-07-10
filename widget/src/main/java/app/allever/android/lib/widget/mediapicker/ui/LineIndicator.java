package app.allever.android.lib.widget.mediapicker.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import app.allever.android.lib.core.app.App;
import app.allever.android.lib.core.helper.DisplayHelper;
import app.allever.android.lib.widget.R;


/**
 *
 */

public class LineIndicator extends View {

    private float mWidth;
    private float mHeight;

    private int mPageCount;
    private float mPageLength;

    private String mPkgName;

    private int mCurrentPage;

    private Paint mPaint;

    private float mOffset;

    private int mCursorColor = App.context.getResources().getColor(R.color.indicator_cursor_color);
    private int mLineColor = App.context.getResources().getColor(R.color.indicator_line_color);
    // 半径
    private float mCursorSize = DisplayHelper.INSTANCE.dip2px(3f);

    private float mMaxOffset;

    private GestureDetector mGestureDetector;
    private SeekBarGestureListener mGestureListener;

    private OnPageChangeListener mOnPageChangeListener;

    private boolean mIsTouch = false;


    public LineIndicator(Context context) {
        this(context, null);
    }

    public LineIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mMaxOffset = mWidth - mCursorSize * 2;
        mPageLength = mMaxOffset / (mPageCount - 1);
        mOffset = mCurrentPage * mPageLength;
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mGestureListener = new SeekBarGestureListener();
        mGestureDetector = new GestureDetector(context, mGestureListener);
        mCurrentPage = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(mLineColor);
        canvas.drawLine(0, mHeight / 2, mWidth, mHeight / 2, mPaint);

        mPaint.setColor(mCursorColor);
        canvas.drawCircle(mOffset + mCursorSize, mHeight / 2, mCursorSize, mPaint);

    }

    public void setColor(int cursorColor, int lineColor) {
        this.mCursorColor = cursorColor;
        this.mLineColor = lineColor;
    }

    private void updateOffset(float x) {
        mOffset = x - x % mPageLength;
        if (mOffset < 0)
            mOffset = 0;
        if (mOffset > mMaxOffset) {
            mOffset = mMaxOffset;
        }
        mCurrentPage = (int) (mOffset / mPageLength);
        invalidate();
        if (mOnPageChangeListener != null) {
            mOnPageChangeListener.onPageChange(mCurrentPage);
        }
    }

    public void moveTo(int page) {
        mCurrentPage = page;
        mOffset = mCurrentPage * mPageLength;
        invalidate();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            updateOffset(event.getX());
            mIsTouch = false;
            return true;
        }
        return mGestureDetector.onTouchEvent(event);
    }

    public boolean isTouch() {
        return mIsTouch;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        mOnPageChangeListener = onPageChangeListener;
    }

    public void setPageCount(int pageCount) {
        mPageCount = pageCount;
    }

    public String getPkgName() {
        return mPkgName;
    }

    public void setPkgName(String pkgName) {
        mPkgName = pkgName;
    }

    public void setCurrentPage(int currentPage) {
        mCurrentPage = currentPage;
    }

    public interface OnPageChangeListener {
        void onPageChange(int page);
    }

    private class SeekBarGestureListener extends
            GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            updateOffset(e2.getX());
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            mIsTouch = true;
            return true;
        }
    }
}
