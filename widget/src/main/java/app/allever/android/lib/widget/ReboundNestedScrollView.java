package app.allever.android.lib.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.TranslateAnimation;

import androidx.core.widget.NestedScrollView;

/**
 * ================================
 * Des: 阻尼回弹 DampingReboundNestedScrollView
 * Created by kele on 2021/2/22.
 * E-mail:984127585@qq.com
 * ================================
 * Android 阻尼回弹效果简单实现（类似iOS）
 * https://www.jianshu.com/p/69e13edd9d0f
 */
public class ReboundNestedScrollView extends NestedScrollView {

    // y方向上当前触摸点的前一次记录位置
    private int previousY = 0;
    // y方向上的触摸点的起始记录位置
    private int startY = 0;
    // y方向上的触摸点当前记录位置
    private int currentY = 0;
    // y方向上两次移动间移动的相对距离
    private int deltaY = 0;

    // 第一个子视图
    private View childView;

    // 用于记录childView的初始位置
    private Rect topRect = new Rect();

    //水平移动搞定距离
    private float moveHeight;

    public ReboundNestedScrollView(Context context) {
        this(context,null);

    }

    public ReboundNestedScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ReboundNestedScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFillViewport(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            childView = getChildAt(0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (null == childView) {
            return super.dispatchTouchEvent(event);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) event.getY();
                previousY = startY;

                // 记录childView的初始位置
                topRect.set(childView.getLeft(), childView.getTop(),
                        childView.getRight(), childView.getBottom());
                moveHeight = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                currentY = (int) event.getY();
                deltaY = currentY - previousY;
                previousY = currentY;

                //判定是否在顶部或者滑到了底部
                if((!childView.canScrollVertically(-1)&&(currentY-startY)>0)||(!childView.canScrollVertically(1)&&(currentY-startY)<0)){
                    //计算阻尼
                    float distance = currentY - startY;
                    if (distance < 0) {
                        distance *= -1;
                    }

                    float damping = 0.5f;//阻尼值
                    float height = getHeight();
                    if (height != 0) {
                        if (distance > height) {
                            damping = 0;
                        } else {
                            damping = (height - distance) / height;
                        }
                    }
                    if (currentY - startY < 0) {
                        damping = 1 - damping;
                    }

                    //阻力值限制再0.3-0.5之间，平滑过度
                    damping *= 0.25;
                    damping += 0.25;

                    moveHeight = moveHeight + (deltaY * damping);

                    childView.layout(topRect.left, (int) (topRect.top + moveHeight), topRect.right,
                            (int) (topRect.bottom + moveHeight));
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!topRect.isEmpty()) {
                    //开始回移动画
                    upDownMoveAnimation();
                    // 子控件回到初始位置
                    childView.layout(topRect.left, topRect.top, topRect.right,
                            topRect.bottom);
                }
                //重置一些参数
                startY = 0;
                currentY = 0;
                topRect.setEmpty();
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    // 初始化上下回弹的动画效果
    private void upDownMoveAnimation() {
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f,
                childView.getTop(), topRect.top);
        animation.setDuration(600);
        animation.setFillAfter(true);
        //设置阻尼动画效果
        animation.setInterpolator(new DampInterpolator());
        childView.setAnimation(animation);
    }

    public class DampInterpolator implements Interpolator {
        @Override
        public float getInterpolation(float input) {
            //没看过源码，猜测是input是时间（0-1）,返回值应该是进度（0-1）
            //先快后慢，为了更快更慢的效果，多乘了几次，现在这个效果比较满意
            return 1 - (1 - input) * (1 - input) * (1 - input) * (1 - input) * (1 - input);
        }
    }
}