package app.allever.android.lib.core.util;

import android.app.Service;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;


public final class SoftKeyboardUtils {

    private SoftKeyboardUtils() {
        // no instance.
    }

    public static boolean isShown(Context context) {
        InputMethodManager inputManager = getInputManager(context);
        return inputManager != null && inputManager.isAcceptingText();
    }

    @Nullable
    private static InputMethodManager getInputManager(Context context) {
        if (context == null) {
            return null;
        }

        return (InputMethodManager) context.getSystemService(Service.INPUT_METHOD_SERVICE);
    }

    public static void showSoftKeyboard(final View view) {
        showSoftKeyboard(view, 100, false);
    }

    public static void showSoftKeyboard(final View view, final long delayedTime, final boolean force) {
        if (view == null) {
            return;
        }

        final InputMethodManager inputMethodManager = getInputManager(view.getContext());
        if (inputMethodManager == null) {
            return;
        }
        UIKit.postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        view.requestFocus();
                        view.setFocusable(true);
                        inputMethodManager.showSoftInput(view, InputMethodManager.RESULT_UNCHANGED_SHOWN);
                    }
                },
                delayedTime
        );
    }

    public static void showSoftKeyboard(final View view, final long delayedTime) {
        showSoftKeyboard(view, delayedTime, false);
    }

    public static void hideSoftKeyboard(final View view) {
        hideSoftKeyboard(view, 0);
    }

    public static void hideSoftKeyboard(final View view, final long delayedTime) {
        if (view == null) {
            return;
        }

        final InputMethodManager inputMethodManager = getInputManager(view.getContext());
        if (inputMethodManager == null) {
            return;
        }
        if (delayedTime == 0) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } else {
            UIKit.postDelayed(new Runnable() {
                @Override
                public void run() {
                    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }, delayedTime);
        }
    }
}
