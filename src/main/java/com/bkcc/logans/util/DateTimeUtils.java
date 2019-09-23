package com.bkcc.logans.util;

import org.apache.commons.lang3.time.FastDateFormat;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 【描 述】：日期时间相关工具类
 * 【环 境】：J2SE 1.8
 *
 * @author 陈汝晗
 * @version v1.0 Apr 25, 2019 新建
 * @since Apr 25, 2019
 */
public class DateTimeUtils {

    /**
     * 【描 述】：转变日期格式
     *
     * @param date    需要转化的日期
     * @param format1 当前格式化format1
     * @param format2 需要转变日期format2
     * @return
     * @throws ParseException
     * @since May 30, 2019
     */
    public static String changeTimeFormat(String date, String format1, String format2) throws ParseException {
        return FastDateFormat.getInstance(format2).format(FastDateFormat.getInstance(format1).parse(date));
    }

    /**
     * 【描 述】：格式化日期
     *
     * @param date
     * @return
     * @since Apr 25, 2019
     */
    public static String formatDate() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 【描 述】：格式化日期
     *
     * @param date
     * @return
     * @since Apr 25, 2019
     */
    public static String formatDate(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }


    /**
     * 【描 述】：格式化日期
     *
     * @param date
     * @param format
     * @return
     * @since Apr 25, 2019
     */
    public static String formatDate(Date date, String format) {
        return FastDateFormat.getInstance(format).format(date);
    }

    /**
     * 【描 述】：查询近期之后的日期
     *
     * @since Jan 15, 2019 v1.0
     */
    public static String getAfterDays(int recentDay) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, recentDay);
        String endTime = FastDateFormat.getInstance("yyyy-MM-dd").format(cal.getTime());
        return endTime;
    }

    /**
     * 【描 述】：查询近期之前的日期
     *
     * @since Jan 15, 2019 v1.0
     */
    public static String getBeforeDays(int recentDay) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -recentDay);
        String beginTime = FastDateFormat.getInstance("yyyy-MM-dd").format(cal.getTime());
        return beginTime;
    }

    private DateTimeUtils() {
    }
}///:~
