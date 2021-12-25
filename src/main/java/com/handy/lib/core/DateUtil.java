package com.handy.lib.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 时间工具类
 *
 * @author handy
 * @since 1.5.8
 */
public class DateUtil {

    /**
     * 简化格式
     */
    public final static String YYYY = "yyyy-MM-dd";

    /**
     * 完整格式
     */
    public final static String YYYY_HH = "yyyy-MM-dd HH:mm:ss";

    /**
     * 字符串转date
     *
     * @param str    字符串
     * @param format 格式
     * @return date
     */
    public static Date parse(String str, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * date转字符串
     *
     * @param date   时间
     * @param format 格式
     * @return 字符串
     */
    public static String format(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * 通过时间秒毫秒数判断两个时间的间隔
     *
     * @param dateTime 时间
     * @return 时间间隔
     */
    public static int getDifferDay(Long dateTime) {
        return (int) ((System.currentTimeMillis() - dateTime) / (1000 * 3600 * 24));
    }

    /**
     * 时间增加
     *
     * @param day 增加天数
     * @return 时间
     */
    public static Date getDate(Integer day) {
        Date date = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        //把日期往后增加一天.整数往后推,负数往前移动
        calendar.add(Calendar.DATE, day);
        return calendar.getTime();
    }

    /**
     * 判断传入的时间是否大于2120-01-01 00:00:00
     *
     * @param date 日期
     * @return true 是
     */
    public static boolean isPerpetual(Date date) {
        return date.getTime() > 4733481600000L;
    }

    /**
     * 获取今日日期
     *
     * @return 今日日期
     */
    public static Date getToday() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = sdf.parse(sdf.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获得指定日期是星期几
     *
     * @param date 日期
     * @return 对应的星期
     * @since 2.2.4
     */
    public static Integer dayOfWeekEnum(Date date) {
        Integer[] weekDays = {7, 1, 2, 3, 4, 5, 6};
        Calendar calendar = Calendar.getInstance();
        return weekDays[calendar.get(Calendar.DAY_OF_WEEK) - 1];
    }

}