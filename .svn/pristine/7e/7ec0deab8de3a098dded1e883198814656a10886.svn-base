package com.telek.hemsipc.util;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author wangxb
 * @date 20-1-10 下午3:12
 */
public class DateUtil {
    public final static String dateformat = "yyyy-MM-dd";
    public static final String MMddHHmmformat = "MM-dd HH:mm";
    public final static String timeformat = "yyyy-MM-dd HH:mm:ss";
    public final static String timeformat2 = "yyyy/MM/dd HH:mm:ss";
    public final static String timeformat3 = "yyyy-MM-dd HH:mm";
    public final static String timeformat4 = "yyyy/MM/dd HH:mm";
    public final static String regexDateStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";
    public static final String yyyyMMddHHmm = "yyyyMMddHHmm";

    private static final Logger LOG = Logger.getLogger(DateUtil.class);

    public static Date dateParse(String dateStr, String formatStr) {
        SimpleDateFormat dateformat = new SimpleDateFormat(formatStr);
        try {
            return dateformat.parse(dateStr);
        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    public static String dateFormat(Date date) {
        return dateFormat(date, dateformat);
    }

    public static String dateFormat(Date date, String formatStr) {
        SimpleDateFormat dateformat = new SimpleDateFormat(formatStr);
        try {
            return dateformat.format(date);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 末尾带一个星期几
     *
     * @param date
     * @param formatStr
     * @return
     */
    public static String dateFormatWithWeek(Date date, String formatStr) {
        String dateformat = dateFormat(date, formatStr);
        int week = getDayOfWeek(date.getTime());
        return dateformat + " " + week;
    }

    public static String dateFormatWithWeek(String dateStr, String formatStr) {
        Date date = dateParse(dateStr, formatStr);
        try {
            int week = getDayOfWeek(date.getTime());
            return dateStr + " " + week;
        } catch (Exception e) {
            e.printStackTrace();
            return dateStr;
        }
    }

    /**
     * 判断某一天是星期几
     *
     * @param time
     * @return
     */
    public static int getDayOfWeek(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date(time));
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    public static String formatA15DateTime(String time) {
        if (!StringUtil.isBlank(time) && time.length() > 2) {
            time = "20" + time;
            Date date = DateUtil.dateParse(time, DateUtil.timeformat4);
            return DateUtil.dateFormat(date, DateUtil.timeformat3);
        }
        return null;
    }
}
