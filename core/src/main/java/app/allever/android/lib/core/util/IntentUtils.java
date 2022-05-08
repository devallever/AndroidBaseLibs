package app.allever.android.lib.core.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import app.allever.android.lib.core.R;


/**
 * 启动Activity
 */

public class IntentUtils {
    /**
     * 使用动画启动Activity
     *
     * @param ctx
     * @param intent
     */
    public static void startActivityWithAnim(@NonNull Context ctx, @NonNull Intent intent) {
        if (ctx instanceof Activity) {
            ctx.startActivity(intent);
            ((Activity) ctx).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        }
    }

    /**
     * 使用动画启动Activity
     *
     * @param fragment
     * @param intent
     */
    public static void startActivityWithAnim(@NonNull Fragment fragment, @NonNull Intent intent) {
        fragment.startActivity(intent);
        fragment.getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 使用动画启动Activity
     *
     * @param act
     * @param intent
     */
    public static void startActivityFroResultWithAnim(Activity act, Intent intent, int requestCode) {
        act.startActivityForResult(intent, requestCode);
        act.overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 使用动画启动Activity
     *
     * @param fragment
     * @param intent
     */
    public static void startActivityFroResultWithAnim(@NonNull Fragment fragment, Intent intent, int requestCode) {
        fragment.startActivityForResult(intent, requestCode);
        fragment.getActivity().overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }

    /**
     * 关闭activity
     *
     * @return
     */
    public static void closeActivity(Context ctx) {
        if (ctx != null) {
            ((Activity) ctx).finish();
            ((Activity) ctx).overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
        }
    }

    /**
     * 打开新的activity
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static void startActivity(Context ctx, Class target) {
        Intent intent = new Intent();
        intent.setClass(ctx, target);
        if (!(ctx instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        ctx.startActivity(intent);
        ((Activity) ctx).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);

    }

    public static void startActivity(Context ctx, Intent intent) {
        if (!(ctx instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        ctx.startActivity(intent);
        if (ctx instanceof Activity) {
            ((Activity) ctx).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
        }
    }

    public static void startActivity(Context ctx, Class target, Bundle bundle, int... flags) {
        Intent intent = new Intent();
        intent.setClass(ctx, target);
        intent.putExtras(bundle);
        if (flags != null && flags.length > 0) {
            for (int flag : flags) {
                intent.addFlags(flag);
            }
        }
        if (!(ctx instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        ctx.startActivity(intent);
        ((Activity) ctx).overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
    }


}
