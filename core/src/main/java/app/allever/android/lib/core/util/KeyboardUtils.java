package app.allever.android.lib.core.util;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.MessageQueue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import app.allever.android.lib.core.app.App;

public class KeyboardUtils {

    /**
     * 打开软键盘
     */
    public static void openKeyboard(EditText mEditText, Context mContext) {
        mEditText.setFocusable(true);
        mEditText.setFocusableInTouchMode(true);
        mEditText.requestFocus();

        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    /**
     * 关闭软键盘
     */
    public static void closeKeyboard(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    //---------

    /**
     * 关闭软键盘
     */
    public static void hideInput(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * 判断当前软键盘是否打开
     */
    public static boolean isSoftInputShow(Activity activity) {

        // 虚拟键盘隐藏 判断view是否为空
        View view = activity.getWindow().peekDecorView();
        if (view != null) {
            // 隐藏虚拟键盘
            InputMethodManager inputmanger = (InputMethodManager) activity
                    .getSystemService(INPUT_METHOD_SERVICE);
//       inputmanger.hideSoftInputFromWindow(view.getWindowToken(),0);

            return inputmanger.isActive() && activity.getWindow().getCurrentFocus() != null;
        }
        return false;
    }

    /**
     * 关闭虚拟键盘
     *
     * @param context 程序上下文
     * @param ediText 当前对应输入框 会出现内存泄漏
     */
    public static void hideKeyboardAsync(Context context, EditText ediText) {
        if (null == ediText || null == context) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            App.mainHandler.post(new HideRunnable(imm, ediText));
        }
    }

    /**
     * 立即关闭虚拟键盘
     *
     * @param context 程序上下文
     * @param ediText 当前对应输入框
     */
    public static void hideKeyboard(Context context, EditText ediText) {
        if (null == ediText || null == context) {
            return;
        }
        final InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(ediText.getWindowToken(), 0);
        }
    }

    //---------

    /**
     * 打开虚拟键盘
     *
     * @param context 程序上下文
     * @param ediText 当前对应输入框
     */
    public static void showKeyboard(Context context, final EditText ediText, int delayMillis) {
        if (null == ediText || null == context) {
            return;
        }
        final InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            ediText.requestFocus();
            ediText.postDelayed(new Runnable() {

                @Override
                public void run() {
                    imm.showSoftInput(ediText, 0);
                }
            }, delayMillis);
        }
    }

    /**
     * 无延迟打开虚拟键盘
     *
     * @param context 程序上下文
     * @param ediText 当前对应输入框
     */
    public static void showKeyboardAsync(Context context, EditText ediText) {
        if (null == ediText || null == context) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            App.mainHandler.post(new ShowRunnable(imm, ediText));
        }
    }

    public static void fixFocusedViewLeak(Application application) {

        // Don't know about other versions yet.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1 || Build.VERSION.SDK_INT > 23) {
            return;
        }

        final InputMethodManager inputMethodManager =
                (InputMethodManager) application.getSystemService(Context.INPUT_METHOD_SERVICE);

        final Field mServedViewField;
        final Field mHField;
        final Method finishInputLockedMethod;
        final Method focusInMethod;
        try {
            mServedViewField = InputMethodManager.class.getDeclaredField("mServedView");
            mServedViewField.setAccessible(true);
            mHField = InputMethodManager.class.getDeclaredField("mServedView");
            mHField.setAccessible(true);
            finishInputLockedMethod = InputMethodManager.class.getDeclaredMethod("finishInputLocked");
            finishInputLockedMethod.setAccessible(true);
            focusInMethod = InputMethodManager.class.getDeclaredMethod("focusIn", View.class);
            focusInMethod.setAccessible(true);
        } catch (NoSuchMethodException | NoSuchFieldException unexpected) {
            return;
        }

        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityDestroyed(Activity activity) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

            }

            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                ReferenceCleaner cleaner = new ReferenceCleaner(inputMethodManager, mHField, mServedViewField,
                        finishInputLockedMethod);
                View rootView = activity.getWindow().getDecorView().getRootView();
                ViewTreeObserver viewTreeObserver = rootView.getViewTreeObserver();
                viewTreeObserver.addOnGlobalFocusChangeListener(cleaner);
            }
        });
    }

    static class HideRunnable implements Runnable {

        private WeakReference<EditText> mEditTextWeakRef;
        private InputMethodManager mInputMethodManager;

        public HideRunnable(InputMethodManager inputMethodManager, EditText editTextWeakRef) {
            mInputMethodManager = inputMethodManager;
            mEditTextWeakRef = new WeakReference<>(editTextWeakRef);
        }

        @Override
        public void run() {
            final EditText editText = mEditTextWeakRef.get();
            if (null != editText) {
                mInputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
            }
        }
    }

    static class ShowRunnable implements Runnable {

        private WeakReference<EditText> mEditTextWeakRef;
        private InputMethodManager mInputMethodManager;

        public ShowRunnable(InputMethodManager inputMethodManager, EditText editTextWeakRef) {
            mInputMethodManager = inputMethodManager;
            mEditTextWeakRef = new WeakReference<>(editTextWeakRef);
        }

        @Override
        public void run() {
            final EditText editText = mEditTextWeakRef.get();
            if (null != editText) {
                mInputMethodManager.showSoftInput(editText, 0);
            }
        }
    }

    static class ReferenceCleaner
            implements MessageQueue.IdleHandler, View.OnAttachStateChangeListener,
            ViewTreeObserver.OnGlobalFocusChangeListener {

        private final InputMethodManager inputMethodManager;
        private final Field mHField;
        private final Field mServedViewField;
        private final Method finishInputLockedMethod;

        ReferenceCleaner(InputMethodManager inputMethodManager, Field mHField, Field mServedViewField,
                         Method finishInputLockedMethod) {
            this.inputMethodManager = inputMethodManager;
            this.mHField = mHField;
            this.mServedViewField = mServedViewField;
            this.finishInputLockedMethod = finishInputLockedMethod;
        }

        @Override
        public void onGlobalFocusChanged(View oldFocus, View newFocus) {
            if (newFocus == null) {
                return;
            }
            if (oldFocus != null) {
                oldFocus.removeOnAttachStateChangeListener(this);
            }
            Looper.myQueue().removeIdleHandler(this);
            newFocus.addOnAttachStateChangeListener(this);
        }

        @Override
        public void onViewAttachedToWindow(View v) {
        }

        @Override
        public void onViewDetachedFromWindow(View v) {
            v.removeOnAttachStateChangeListener(this);
            Looper.myQueue().removeIdleHandler(this);
            Looper.myQueue().addIdleHandler(this);
        }

        @Override
        public boolean queueIdle() {
            clearInputMethodManagerLeak();
            return false;
        }

        private void clearInputMethodManagerLeak() {
            try {
                Object lock = mHField.get(inputMethodManager);
                // This is highly dependent on the InputMethodManager implementation.
                synchronized (lock) {
                    View servedView = (View) mServedViewField.get(inputMethodManager);
                    if (servedView != null) {

                        boolean servedViewAttached = servedView.getWindowVisibility() != View.GONE;

                        if (servedViewAttached) {
                            // The view held by the IMM was replaced without a global focus change. Let's make
                            // sure we get notified when that view detaches.

                            // Avoid double registration.
                            servedView.removeOnAttachStateChangeListener(this);
                            servedView.addOnAttachStateChangeListener(this);
                        } else {
                            // servedView is not attached. InputMethodManager is being stupid!
                            Activity activity = extractActivity(servedView.getContext());
                            if (activity == null || activity.getWindow() == null) {
                                // Unlikely case. Let's finish the input anyways.
                                finishInputLockedMethod.invoke(inputMethodManager);
                            } else {
                                View decorView = activity.getWindow().peekDecorView();
                                boolean windowAttached = decorView.getWindowVisibility() != View.GONE;
                                if (!windowAttached) {
                                    finishInputLockedMethod.invoke(inputMethodManager);
                                } else {
                                    decorView.requestFocusFromTouch();
                                }
                            }
                        }
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException unexpected) {
            }
        }

        private Activity extractActivity(Context context) {
            while (true) {
                if (context instanceof Application) {
                    return null;
                } else if (context instanceof Activity) {
                    return (Activity) context;
                } else if (context instanceof ContextWrapper) {
                    Context baseContext = ((ContextWrapper) context).getBaseContext();
                    // Prevent Stack Overflow.
                    if (baseContext == context) {
                        return null;
                    }
                    context = baseContext;
                } else {
                    return null;
                }
            }
        }
    }

    public static class SoftKeyboardListener {
        /**
         * 根视图的显示高度
         */
        private int mRootViewVisibleHeight;
        private OnSoftKeyboardChangeListener mOnSoftKeyboardChangeListener;

        public SoftKeyboardListener(@NonNull Activity activity) {
            //获取activity的根视图
            final View rootView = activity.getWindow().getDecorView();

            //监听视图树中全局布局发生改变或者视图树中的某个视图的可视状态发生改变
            rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    //获取当前根视图在屏幕上显示的大小
                    Rect r = new Rect();
                    rootView.getWindowVisibleDisplayFrame(r);

                    final int visibleHeight = r.height();
                    if (mRootViewVisibleHeight == 0) {
                        mRootViewVisibleHeight = visibleHeight;
                        return;
                    }

                    //根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变
                    if (mRootViewVisibleHeight == visibleHeight) {
                        return;
                    }

                    //根视图显示高度变小超过200，可以看作软键盘显示了
                    if (mRootViewVisibleHeight - visibleHeight > 200) {
                        if (mOnSoftKeyboardChangeListener != null) {
                            mOnSoftKeyboardChangeListener.show(mRootViewVisibleHeight - visibleHeight);
                        }
                        mRootViewVisibleHeight = visibleHeight;
                        return;
                    }

                    //根视图显示高度变大超过200，可以看作软键盘隐藏了
                    if (visibleHeight - mRootViewVisibleHeight > 200) {
                        if (mOnSoftKeyboardChangeListener != null) {
                            mOnSoftKeyboardChangeListener.hide(visibleHeight - mRootViewVisibleHeight);
                        }
                        mRootViewVisibleHeight = visibleHeight;
                        return;
                    }

                }
            });
        }

        public static void setListener(Activity activity, OnSoftKeyboardChangeListener onSoftKeyboardChangeListener) {
            SoftKeyboardListener softKeyboardListener = new SoftKeyboardListener(activity);
            softKeyboardListener.setOnSoftKeyboardChangeListener(onSoftKeyboardChangeListener);
        }

        private void setOnSoftKeyboardChangeListener(OnSoftKeyboardChangeListener onSoftKeyboardChangeListener) {
            this.mOnSoftKeyboardChangeListener = onSoftKeyboardChangeListener;
        }

        public interface OnSoftKeyboardChangeListener {
            void show(int height);

            void hide(int height);
        }
    }
}
