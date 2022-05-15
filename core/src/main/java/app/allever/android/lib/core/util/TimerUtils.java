package app.allever.android.lib.core.util;

import android.os.CountDownTimer;

/**
 * Created by Juzhengyuan
 *
 * @Author: Jerry.
 * @Date: 2020/12/9 10
 * @Desc:
 */
public class TimerUtils {

    public static CountDownTimer createMinutesAndSecondsTimer(int minutes, OnTimerListener onTimerListener) {
        return new CountDownTimer(minutes * 60 * 1000
                , 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (null != onTimerListener) {
                    int totalSeconds = (int) (millisUntilFinished / 1000);
                    int seconds = totalSeconds % 60;
                    int minutes = (totalSeconds / 60) % 60;
                    int hours = totalSeconds / 3600;
                    onTimerListener.onTick(String.format("%02d", hours), String.format("%02d", minutes), String.format("%02d", seconds), millisUntilFinished);
                }
            }

            @Override
            public void onFinish() {
                if (null != onTimerListener) {
                    onTimerListener.onComplete();
                }
            }
        };
    }

    public interface OnTimerListener {
        void onTick(String hour, String minutes, String seconds, long millisUntilFinished);

        void onComplete();
    }
}
