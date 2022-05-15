package app.allever.android.lib.core.helper

import java.text.SimpleDateFormat

object TimeHelper {

    /**
     * 根据传入的分钟转化为对应的天数小时分钟 如 : 1天12小时23分
     *
     * @param minute
     * 分钟
     * @return
     */
    fun getNumTime(minute: Long): String? {
        var days = ""
        days = when {
            minute < 60 -> {
                minute.toString() + "分钟"
            }
            minute < 1440 -> {
                val value = minute % 60
                (((minute - value) / 60).toString() + "小时"
                        + value.toString() + "分钟")
            }
            else -> {
                val minuteValue = minute % 60
                val value = (minute - minuteValue) / 60
                val hourValue = value % 24
                val dayValue = (value - hourValue) / 24
                (dayValue.toString() + "天" + hourValue.toString() + "小时"
                        + minuteValue + "分钟")
            }
        }
        return days
    }

    /**
     * 将毫秒格式化成 00:00:00
     */
    fun formatTime(time: Long): String {
        val simpleDateFormat = SimpleDateFormat("HH:mm:ss")
        return simpleDateFormat.format(time)
    }

    /**
     * 将毫秒格式化成 00:00:00
     */
    fun formatTimeYYYY_MM_DD_HH_MM_SS(time: Long): String {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return simpleDateFormat.format(time)
    }
}