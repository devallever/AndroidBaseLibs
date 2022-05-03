package app.allever.android.lib.core.util;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

import androidx.core.view.ViewCompat;

/**
 * @author allever
 */
public class AnimatorUtils {

    public static void scaleUp(View target) {
        scaleUp(target, 1.08f);
    }

    public static void scaleDown(View target) {
        scaleDown(target, 1.0f);
    }

    //1.08表示放大倍数,可以随便改
    //默认1.08
    public static void scaleUp(View target, float factor) {
        ViewCompat.animate(target)
                .setDuration(200)
                .scaleX(factor)
                .scaleY(factor)
                .start();
    }

    //默认1.0
    public static void scaleDown(View target, float factor) {
        ViewCompat.animate(target)
                .setDuration(200)
                .scaleX(factor)
                .scaleY(factor)
                .start();
    }

    public static void scaleUp2Down(float start, float end, View target) {
        ObjectAnimator objectAnimatorX = ObjectAnimator.ofFloat(target, "scaleX", start, end).setDuration(500);
        ObjectAnimator objectAnimatorY = ObjectAnimator.ofFloat(target, "scaleY", start, end).setDuration(500);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setTarget(target);
        animatorSet.play(objectAnimatorX).with(objectAnimatorY);
        animatorSet.start();
    }

    public static void scaleUpOrDown(View view, boolean isScaleUp) {
        if (isScaleUp) {
            scaleUp(view);
        } else {
            scaleDown(view);
        }
    }

    public static void scaleUpOrDown(View view, boolean isScaleUp, float factor) {
        if (isScaleUp) {
            scaleUp(view, factor);
        } else {
            scaleDown(view);
        }
    }
}
