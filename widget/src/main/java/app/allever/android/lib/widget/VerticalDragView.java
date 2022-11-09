package app.allever.android.lib.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.widget.ListViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.customview.widget.ViewDragHelper;
import androidx.recyclerview.widget.RecyclerView;

/**
 * date  5/25/21  11:01 AM
 * author  DarrenHang
 */
public class VerticalDragView extends FrameLayout {

    private ViewDragHelper mDragHelper;
    //可以拖动的View
    private View mDragView;

    private float mDownY;

    /**
     * 可滚动的控件
     */
    private View mChildCanScrollAbleView;
    private boolean mChildScrolling = false;

    /**
     * 传入内部滚动的子View, 解决滑动冲突
     *
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setChildCanScrollView(View view) {
        mChildCanScrollAbleView = view;
        if (mChildCanScrollAbleView instanceof RecyclerView) {
            ((RecyclerView) mChildCanScrollAbleView).addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        mChildScrolling = !((RecyclerView) mChildCanScrollAbleView).canScrollVertically(-1);
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    mChildScrolling = true;

                }
            });
        } else if (view instanceof ScrollView || view instanceof NestedScrollView){
            mChildCanScrollAbleView.setOnScrollChangeListener(new OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    //TODO 未处理其他滚动控件
                }
            });
        }

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
        if (mChildCanScrollAbleView != null) {
            return !mChildScrolling;
        }

        return mDragView.canScrollVertically(-1);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //拖动View
        mDragView = getChildAt(0);
    }

    private ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(@NonNull View child, int pointerId) {
            //指定子 View 是否可以拖动
            return true;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return top;
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            mDragHelper.settleCapturedViewAt(0, 0);
            invalidate();
        }
    };

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            invalidate();
        }
    }
}