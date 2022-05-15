package app.allever.android.lib.core.util;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;

import java.util.Calendar;
import java.util.TimeZone;

import app.allever.android.lib.core.ext.LoggerKt;

/**
 * @author allever
 */
public class AlarmUtils {

    private static Calendar sCalendar = Calendar.getInstance();

    /**
     * 设置重复闹钟
     *
     * @param context
     * @param hour
     * @param min
     * @param taskId
     */
    public static void setAlarm(Context context, int hour, int min, int taskId, int switchType, long intervalMillis) {
        if (context == null) {
            return;
        }

        sCalendar.clear();


        //是设置日历的时间，主要是让日历的年月日和当前同步
        sCalendar.setTimeInMillis(System.currentTimeMillis());
        // 这里时区需要设置一下
        sCalendar.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        //设置星期
//        sCalendar.set(Calendar.DAY_OF_WEEK, week);
        //设置在几点提醒  设置的为13点
        sCalendar.set(Calendar.HOUR_OF_DAY, hour);
        //设置在几分提醒  设置的为25分
        sCalendar.set(Calendar.MINUTE, min);
        //下面这两个看字面意思也知道
        sCalendar.set(Calendar.SECOND, 0);
        sCalendar.set(Calendar.MILLISECOND, 0);


//        Intent intent = new Intent(context, TimerTaskReceiver.class);
//        intent.setAction(TimerTaskReceiver.ACTION_TIMER_TASK);

        LoggerKt.debugD("setAlarm taskId = " + taskId);
        LoggerKt.debugD("setAlarm switchType = " + switchType);

//        intent.putExtra("taskId", taskId);
//        intent.putExtra("switchType", switchType);

//创建PendingIntent对象封装Intent，由于是使用广播，注意使用getBroadcast方法

//        int requestCode = Integer.parseInt(taskId+""+switchType);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent, 0);

//获取AlarmManager对象

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        int alarmHour = sCalendar.get(Calendar.HOUR_OF_DAY);
        int alarmMin = sCalendar.get(Calendar.MINUTE);
        LoggerKt.debugD("闹钟时间: " + alarmHour + ":" + alarmMin);
        //间隔 intervalMillis
//        alarmManager.setExact(AlarmManager.RTC_WAKEUP, sCalendar.getTimeInMillis(), pendingIntent);
//        有延时0-2分钟
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, sCalendar.getTimeInMillis(), intervalMillis, null);
    }

    /**
     * 取消闹钟
     *
     * @param context
     * @param taskId
     */
    public static void cancelAlarm(Context context, int taskId, int switchType) {
        if (context == null) {
            return;
        }

//        Intent intent = new Intent(context, TimerTaskReceiver.class);
//        intent.setAction(TimerTaskReceiver.ACTION_TIMER_TASK);
//        int requestCode = Integer.parseInt(taskId + "" + switchType);
        PendingIntent pi = PendingIntent.getBroadcast(context, 1, null, 0);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        //取消警报
        am.cancel(pi);
    }
}
