package org.sprintdragon.teleport.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;

/**
 * Created by wangdi on 16-2-22.
 */
public class CommonTimeUtils {
    /**
     * 获取某时间字符串
     */
    public static String getSimpleDateFormatDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date);
    }

    public static String getSimpleDateFormatDateTime(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(new Date(time));
    }

    /**
     * 获取某时间字符串
     */
    public static String getSimpleDateFormatDateTimeWithOutLine(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        return dateFormat.format(date);
    }

    /**
     * 获取当前时间字符串
     */
    public static String getNowDateTime() {
        String timeStampValue = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        timeStampValue = dateFormat.format(new Date());
        return timeStampValue;
    }

    public static Date getDateFromStr(String dateStr) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return dateFormat.parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
    }


    public static Date getMongoDate(Date date) {
        try {
            SimpleDateFormat format =
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            format.setCalendar(new GregorianCalendar(new SimpleTimeZone(0, "GMT")));
            return format.parse(format.format(date));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println(getMongoDate(new Date()));
    }
}
