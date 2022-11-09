package app.allever.android.lib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ListViewCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.recyclerview.widget.RecyclerView;

import app.allever.android.lib.core.helper.DisplayHelper;

/**
 * date  5/25/21  11:01 AM
 * author  DarrenHang
 */
public class VerticalDragView extends FrameLayout {

    private ViewDragHelper mDragHelper;
    //可以拖动的View
    private View mDragView;
    //拖动范围
    int menuHeight = DisplayHelper.INSTANCE.getScreenHeight();

    private float mDownY;
    private boolean mMenuIsOpen = false;

    private ScrollListener mScrollListener;

    public void setScrollListener(ScrollListener scrollListener) {
        mScrollListener = scrollListener;
    }

    public VerticalDragView(@NonNull Context context) {
        this(context, null);
    }

    public VerticalDragView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalDragView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDragHelper = ViewDragHelper.create(this, mCallback);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        if (mMenuIsOpen) {
            return true;
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                mDragHelper.processTouchEvent(ev);
                break;
            case MotionEvent.ACTION_MOVE:
                float offSetY = ev.getY() - mDownY;
                if (offSetY > 0 && !canChildScrollUp()) {
                    return true;
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 判断是否滚动到顶部
     *
     * @return
     */
    public boolean canChildScrollUp() {

        if (mDragView instanceof ListView) {
            return ListViewCompat.canScrollList((ListView) mDragView, -1);
        }
        if (mScrollListener != null) {
            return !mScrollListener.childViewScrolling();
        }

        return mDragView.canScrollVertically(-1);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
//        //获取子View个数
//        int childCount = getChildCount();
//        if (childCount != 2) {
//            throw new RuntimeException("VerticalDragView must include 2 child view");
//        }
        //拖动View
        mDragView = getChildAt(0);
    }


//    @Override
//    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        super.onLayout(changed, left, top, right, bottom);
//        if (changed) {
//            menuHeight = getChildAt(0).getMeasuredHeight();
//        }
//    }

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            //指定子 View 是否可以拖动
            return child == mDragView;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            //垂直滑动移动的位置
            if (top <= 0) {
                top = 0;
            }

            if (top >= menuHeight) {
                top = menuHeight;
            }
            return top;
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            if (mDragView.getTop() > menuHeight) {
                //滚动到菜单的高度（打开）
                mDragHelper.settleCapturedViewAt(0, menuHeight);
                mMenuIsOpen = true;
            } else {
                //滚动到0（关闭）
                mDragHelper.settleCapturedViewAt(0, 0);
                mMenuIsOpen = false;
            }
            invalidate();
        }
    };

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }

    public interface ScrollListener {
        public default boolean childViewScrolling() {
            return false;
        }
    }
}