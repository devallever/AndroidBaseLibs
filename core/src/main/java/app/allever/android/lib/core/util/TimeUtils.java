package app.allever.android.lib.core.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static final String FORMAT_hh_mm_ss = "HH:mm:ss";
    public static final String FORMAT_yyyy_MM_dd = "yyyy-MM-dd";
    public static final String FORMAT_MM_dd = "MM-dd";

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;


    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";

    public static String getMMSS(Long time) {
        long min = time / (60 * 1000);
        long sec = (time / (1000)) % 60;
        String secStr = sec < 10 ? "0" + sec : sec + "";
        return min + ":" + secStr;
    }

    /**
     * 格式化时间
     *
     * @param time
     * @param format
     * @return
     */
    public static String formatTime(long time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }

    public static String timeCount(long time) {
        return format(new Date(time));
    }


    /**
     * 计算几分钟前
     *
     * @param dateTime
     * @return
     */
    public static String timeCount(Date dateTime) {
        return format(dateTime);
    }

    public static String format2_0(Date date) {
        long delta = System.currentTimeMillis() - date.getTime();
        //小于24小时
        if (delta < 24L * ONE_HOUR) {
            return formatTime(date.getTime(), FORMAT_yyyy_MM_dd);
        }
        if (delta < 7L * ONE_DAY) {
            return "1" + ONE_DAY_AGO;
        } else {
            return "7" + ONE_DAY_AGO;
        }
    }

    //时间转换
    private static String format(Date date) {
        long delta = System.currentTimeMillis() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }

    public static boolean isToday(Date inputJudgeDate) {
        boolean flag = false;
        // 获取当前系统时间
        long longDate = System.currentTimeMillis();
        Date nowDate = new Date(longDate);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = dateFormat.format(nowDate);
        String subDate = format.substring(0, 10);
        // 定义每天的24h时间范围
        String beginTime = subDate + " 00:00:00";
        String endTime = subDate + " 23:59:59";
        Date paseBeginTime = null;
        Date paseEndTime = null;
        try {
            paseBeginTime = dateFormat.parse(beginTime);
            paseEndTime = dateFormat.parse(endTime);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (inputJudgeDate.after(paseBeginTime) && inputJudgeDate.before(paseEndTime)) {
            flag = true;
        }
        return flag;
    }

    /**
     * @param oldTime 较小的时间
     * @return -1 ：同一天.    0：昨天 .   1 ：至少是前天.
     * @throws ParseException 转换异常
     * @author LuoB.
     */
    public static boolean isYesterday(Date oldTime) {


        Date newTime = new Date();

        //将下面的 理解成  yyyy-MM-dd 00：00：00 更好理解点
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String todayStr = format.format(newTime);
        Date today = null;
        try {
            today = format.parse(todayStr);
            if (today == null) {
                return false;
            }
            //昨天 86400000=24*60*60*1000 一天
            if ((today.getTime() - oldTime.getTime()) > 0 && (today.getTime() - oldTime.getTime()) <= 86400000) {
                return true;
            } else if ((today.getTime() - oldTime.getTime()) <= 0) { //至少是今天
                return false;
            } else { //至少是前天
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return false;
    }

    /**
     *
     * APP业务相关的显示时间逻辑
     *
     * @param date
     * @return
     */
    /***
     * 5、发私信时间：
     *
     *  5.1 今天发的私聊消息，按以下规则显示：
     *
     *       最新收发，5分钟内发布的显示：刚刚；
     *
     *       5分钟前发布的显示： 5分钟前；
     *
     *       半小时前发布的显示：30分钟前；
     *
     *       1~2小时内发布的显示：1小时前；
     *
     *       2~3小时内发布的显示：2小时前；
     *
     *       当天内，3小时后发布的统一显示：3小时后；
     *
     *  5.2  昨天发的，显示日期格式：mm-dd；
     *
     *  5.3  1天，显示：1天前；
     *
     *  5.4   2天前 ~ 7天内 发的，显示：7天前；
     *
     *  5.5  7天前 ~ 1个月内 发的，显示：1月前；
     *
     *  5.6  1个月后发的统一显示：1年前；
     */
    public static String timeCountWithAppBusiness(long time) {
        Date date = new Date(time);
        long delta = System.currentTimeMillis() - time;
        //最新收发，5分钟内发布的显示：刚刚；
        if (delta < 5L * ONE_MINUTE) {
            return "刚刚";
        }
        //5分钟-30分钟发布的显示： 5分钟前；
        if (delta < 30L * ONE_MINUTE) {
            return "5分钟前";
        }
        //30分钟-1小时前发布的显示：30分钟前；
        if (delta < 1L * ONE_HOUR) {
            return "30分钟前";
        }
        //1~2小时内发布的显示：1小时前；
        if (delta < 2L * ONE_HOUR) {
            return "1小时前";
        }
        //2~3小时内发布的显示：2小时前；
        if (delta < 3L * ONE_HOUR) {
            return "2小时前";
        }

        //当天内，3小时后发布的统一显示：3小时后；
        if (TimeUtils.isToday(date)) {
            return "3小时前";
        }
        if (TimeUtils.isYesterday(date)) {
            //昨天发的，显示日期格式：mm-dd；
            return TimeUtils.formatTime(date.getTime(), TimeUtils.FORMAT_MM_dd);
        }
        //1天，显示：1天前；
        if (delta < 1L * ONE_DAY) {
            return "1天前";
        }
        //2天前 ~ 7天内 发的，显示：7天前；
        if (delta < 7L * ONE_DAY) {
            return "7天前";
        }
        //7天前 ~ 1个月内 发的，显示：1月前；
        if (delta < 30L * ONE_DAY) {
            return "1个月前";
        }
        //1个月后发的统一显示：1年前；
        return "1年前";
    }

    public static String getTimeFormat(String time) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy.MM.dd");
        return dateFormat2.format(dateFormat.parse(time));
    }

}
