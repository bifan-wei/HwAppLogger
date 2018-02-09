package com.hw.applogger;

import android.annotation.SuppressLint;
import android.net.ParseException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * TODO 时间工具类
 * --------------------
 * 2017年8月25日下午5:00:49
 */
 class TimeUtil {
    private static final String[] weekOfDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
    /**
     * @return 返回当前年
     */
    public static int CurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * @return 返回当前月
     */
    public static int CurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * @return 返回当前几号
     */
    public static int CurrentDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @return 返回这个月的最小那天是几号
     */
    public static int CurrentMonthMinDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
    }


    /**
     * @return 返回这个月的最大那天是几号
     */
    public static int CurrentMonthMaxDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * @return 返回当前月份的所有天号数
     */
    public static List<Integer> CurrentMonthDays() {
        int min = CurrentMonthMinDay();
        int max = CurrentMonthMaxDay();

        List<Integer> days = new ArrayList<>();

        for (int i = min; i <= max; i++) {
            days.add(i);
        }
        return days;
    }


    /**
     * @return 返回这个月剩余天数，可能返回0
     */
    public static int getThisMonthLeftDays() {
        int currentDayInMonth = CurrentDay();
        int maxDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        int left = maxDay - currentDayInMonth;
        if (left <= 0) {
            left = 0;
        }
        return left;
    }

    /**
     * @return 获取今天星期几
     */
    public static String getDayInWeek() {
        Calendar calendar = Calendar.getInstance();
        int w = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekOfDays[w];
    }


    /**
     * 获取当前系统时间，格式为yyyyMMdd HH:mm:ss
     */
    public static String getCurrentDateString() {
        return getCurrentDateString("yyyyMMdd HH:mm:ss");
    }

    /**
     * @param form 指定时间格式
     * @return getCurrentDateString
     */
    public static String getCurrentDateString(String form) {
        return getTimeByDate(getCurrentDate(), form);
    }

    /**
     * @param date date
     * @param form form
     * @return 指定date与时间格式获取时间
     */
    @SuppressLint("SimpleDateFormat")
    public static String getTimeByDate(Date date, String form) {
        SimpleDateFormat simpleFormatter = new SimpleDateFormat(form);
        return simpleFormatter.format(date);
    }

    /**
     * @param Time 当前格式的时间字符串
     * @param form 转为为该格式
     * @return 将某个格式时间转化为另外格式的时间
     */
    public static String getTimeByString(String Time, String form) {

        SimpleDateFormat sdf = new SimpleDateFormat(form);

        try {
            return getTimeByDate(sdf.parse(Time), form);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return Time;
    }

    /**
     * @param Time       Time
     * @param fromFormat 当前时间格式
     * @param toFormat   要转为的时间格式
     * @return 将某个格式时间转化为另外格式的时间
     */
    public static String getTimeByString(String Time, String fromFormat, String toFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(fromFormat);

        try {
            return getTimeByDate(sdf.parse(Time), toFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Time;
    }

    public static String getTimeFormTimeMillis(long TimeMillis, String form) {
        return new SimpleDateFormat(form).format(new Date(TimeMillis));
    }

    /**
     * @return 获取当前时间date
     */
    public static Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 获取距离当前时间多少天的日期
     *
     * @param add 距离的时间天数,正数就增加，负数就减少
     * @return
     */
    public static String getTimefarFromCurrentdateByDay(int add) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, add);
        return getTimeByDate(calendar.getTime(), "yyyyMMdd");

    }

    /**
     * 获取距离当前时间多少天的日期
     *
     * @param add 距离的时间天数,正数就增加，负数就减少
     * @return
     */
    public static String getTimefarFromCurrentdateByDay(int add, String toForm) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, add);
        return getTimeByDate(calendar.getTime(), toForm);

    }

    /**
     * 获取距离当前时间多少月的日期
     *
     * @param add 距离的时间月数,正数就增加，负数就减少
     * @return
     */
    public static String getTimeFarFromCurrentDateByMonth(int add) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, add);
        return getTimeByDate(calendar.getTime(), "yyyyMMdd");
    }

    public static String getTimeFarFromCurrentDateByMonth(int add, String form) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, add);
        return getTimeByDate(calendar.getTime(), form);
    }

    public static int getMonthFarFromCurrentDateByMonth(int add) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, add);
        return calendar.getTime().getMonth() + 1;
    }

    /**
     * 计算两个日期相差的月份数:如果date2日期比date1早，将返回负数
     *
     * @param date1   日期1
     * @param date2   日期2
     * @param pattern 日期1和日期2的日期格式
     * @return 相差的月份数
     * @throws ParseException
     */
    public static int countMonths(String date1, String date2, String pattern) throws java.text.ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(sdf.parse(date1));
        c2.setTime(sdf.parse(date2));
        int year = c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR);
        return year * 12 + c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
    }

    /**
     * @param Duration Duration
     * @return --------------------
     * TODO 秒时间转为有单位时间，单位是中文单位
     * --------------------
     */
    public static String GetTimeFromSecondWithChinese(int Duration) {
        if (Duration < 0) {
            return "0秒";
        }

        if (Duration < 60) {
            return Duration + "秒";
        }

        if (Duration < 3600) {
            return Math.round((float) Duration / 60) + "分钟";
        }

        return FormatUtil.getFloat_KeepOneDecimalplaces(((float) Duration) / 3600) + "小时";
    }

    /**
     * @param Duration Duration
     * @return --------------------
     * TODO 秒时间转为有单位时间，单位是英文单位
     * --------------------
     */
    public static String GetTimeFromSecondWithEnglish(int Duration) {
        if (Duration < 0) {
            return "0s";
        }
        if (Duration < 60) {
            return Duration + "s";
        }

        if (Duration < 3600) {
            return Math.round((float) Duration / 60) + "min";
        }
        return FormatUtil.getFloat_KeepOneDecimalplaces(((float) Duration) / 3600) + "h";
    }

}
