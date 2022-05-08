package app.allever.android.lib.core.util;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * 公共工具类
 *
 * @Author Jerry
 * @create at 2020.03.04 12:17
 */
public class ObjectUtils {

    public static final int PHONE_LENGTH = 11;

    /**
     * double 保留小数点位数
     *
     * @param num     原始的数量
     * @param pattern 保留位数的格式
     */
    public static String keepDecimalPlaces(double num, String pattern) {
        DecimalFormat df = new DecimalFormat(pattern);
        if (0 > num) {
            return 1 > num ? "-0" + df.format(Math.abs(num)) : "-" + df.format(Math.abs(num));
        } else {
            return 1 > num ? "0" + df.format(num) : df.format(num);
        }
    }

    /**
     * double 保留2位小数点
     *
     * @param num 原始的数量
     */
    public static String keep2DecimalPlaces(double num) {
        return keepDecimalPlaces(num, "#.00");
    }


    /**
     * 设置富文本适配手机屏幕
     *
     * @param content 富文本内容
     */
    public static String setWebViewContent(String content) {
        return "<html>\n" +
                "    <head>\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                "        <style>img{max-width: 100%; width:100%; height:auto;}*{margin:0px;}</style>\n" +
                "    </head>\n" +
                "    <body>" + content.trim() + " </body></html>";
    }

    /**
     * 设置时间戳格式
     *
     * @param format            输出时间格式
     * @param currentTimeMillis 时间戳
     */
    public static String setTime2Format(String format, long currentTimeMillis) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(currentTimeMillis);
    }

    /**
     * 判断字符串是否符合手机号码格式
     * 移动号段: 134,135,136,137,138,139,147,150,151,152,157,158,159,170,178,182,183,184,187,188
     * 联通号段: 130,131,132,145,155,156,170,171,175,176,185,186
     * 电信号段: 133,149,153,170,173,177,180,181,189
     *
     * @param mobile Num
     * @return 待检测的字符串
     */
    public static boolean isMobileNO(String mobileNum) {
        // "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String telRegex = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$";
        if (TextUtils.isEmpty(mobileNum)) {
            return false;
        } else {
            return mobileNum.matches(telRegex);
        }
    }

    /**
     * 将手机号码18033339999 转换为 180****9999
     *
     * @param phoneNumber
     * @return
     */
    public static String encryptMobile(String phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            phoneNumber = phoneNumber.trim();
            if (PHONE_LENGTH == phoneNumber.length()) {
                String encrypt = "****";
                String encryptStr = phoneNumber.substring(3, 7);
                return phoneNumber.replace(encryptStr, encrypt);
            }
        }
        return phoneNumber;
    }

}
