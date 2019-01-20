package com.greenbee.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateLib {
    public static String getDate(Date date) {
        return getDate("yyyy-MM-dd'T'HH:mm:ss.S Z", date);
    }

    public static String getDate() {
        return getDate(new Date());
    }

    public static String getLogTimeStamp() {
        return getDate("yyyy-MM-dd HH:mm:ss", new Date());
    }

    public static String getDate(String format, Date date) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(date);
    }

    public static Date parseDate(String format, String date) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.parse(date);
    }
}
