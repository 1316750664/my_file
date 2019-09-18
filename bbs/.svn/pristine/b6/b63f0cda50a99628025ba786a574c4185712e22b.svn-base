package com.util.tools;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * User: hzm
 * Date: 14-5-4
 * Time: 上午10:14
 */
public class FormatDate {

    /**
     * @param f
     * @return String 时间格式
     * @readme 当前时间, 自己定义格式
     */
    public static String getFormatTimeString(String f) {
        java.util.Date d = new java.util.Date();
        return getFormat(f).format(d);
    }

    /**
     * 同时返回两种格式的时间字符串数组
     *
     * @param f1
     * @param f2
     * @return
     */
    public static String[] getFormatTimeString(String f1, String f2) {
        java.util.Date d = new java.util.Date();
        return new String[]{getFormat(f1).format(d), getFormat(f2).format(d)};
    }

    /**
     * @param f String
     * @return SimpleDateFormat
     * @readme 传入参数, 时间格式.返回SimpleDateFormat对像
     */
    public static SimpleDateFormat getFormat(String f) {
        SimpleDateFormat sdf = new SimpleDateFormat(f);
        return sdf;
    }

    public static String getTimestampString(Timestamp timestamp) {
        if (timestamp == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(timestamp);
    }

    public static String getDateString(Date date) {
        if (date == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    public static String getTimeString(Time time) {
        if (time == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(time);
    }

    public static long getStringTimestamp(String datetime) {
        if (datetime == null || "".equals(datetime)) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return sdf.parse(datetime).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    public static long getStringDate(String datetime) {
        if (datetime == null || "".equals(datetime)) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sdf.parse(datetime).getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 比较当前日期和指定日期 return boolean
     * 如果当前日期大于指定日期返回true否则返回flase
     */
    public static boolean dateCompare(String str) {
        boolean bea = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String isDate = sdf.format(new java.util.Date());
        java.util.Date date1;
        java.util.Date date0;
        try {
            date1 = sdf.parse(str);
            date0 = sdf.parse(isDate);
            if (date0.after(date1)) {
                bea = true;
            }
        } catch (ParseException e) {
            System.out.print("dateCompare:" + e.getMessage());
        }
        return bea;
    }


    /**
     * 比较当前日期和指定日期,注：要比较日期的格式要跟当前日期格式一致
     *
     * @param StrDate:要比较日期的字符串
     * @param StrDateFormat:当前日期格式
     * @return boolean 如果当前日期大于指定日期返回true否则返回flase
     */
    public static boolean dateCompare(String StrDate, String StrDateFormat) {
        boolean bea = false;
        SimpleDateFormat sdf = new SimpleDateFormat(StrDateFormat);
        String isDate = sdf.format(new java.util.Date());
        java.util.Date date1;
        java.util.Date date0;
        try {
            date1 = sdf.parse(StrDate);
            date0 = sdf.parse(isDate);
            if (date0.after(date1)) {
                bea = true;
            }
        } catch (ParseException e) {
            System.out.print("dateCompare:" + e.getMessage());
        }
        return bea;
    }

    /**
     * 比较当前日期和指定日期，如果当前日期晚则返回true
     *
     * @param date       待比较日期
     * @param dateFormat 日期格式
     * @return
     */
    public static boolean dateCompare(java.util.Date date, String dateFormat) {
        boolean bea = false;
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String oriDate = sdf.format(date);
        String nowDate = sdf.format(new java.util.Date());
        java.util.Date date1;
        java.util.Date date0;
        try {
            date1 = sdf.parse(oriDate);
            date0 = sdf.parse(nowDate);
            if (date0.after(date1)) {
                bea = true;
            }
        } catch (ParseException e) {
            System.out.print("dateCompare:" + e.getMessage());
        }
        return bea;
    }
}
