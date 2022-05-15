package app.allever.android.lib.widget;

import android.view.View;

import app.allever.android.lib.core.ext.LoggerKt;


/**
 * @author allever
 */
public abstract class ClickListener implements View.OnClickListener {

    private static final long TIME_INTERVAL = 500;

    private long lastTime;

    /**
     * @param v
     */
    public abstract void click(View v);

    @Override
    public void onClick(View v) {
        if (checkTime()) {
            LoggerKt.debugD("防止多次点击");
            return;
        }
        this.click(v);
    }

    private boolean checkTime() {
        boolean flag = true;
        long time = System.currentTimeMillis() - lastTime;

        if (time > getTimeInterval()) {
            flag = false;
        }
        lastTime = System.currentTimeMillis();
        return flag;
    }

    /**
     * 设置时间间隔， 默认1秒
     *
     * @return
     */
    protected long getTimeInterval() {
        return TIME_INTERVAL;
    }

}
