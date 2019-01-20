package com.greenbee.commons;

public class StringUtils {

    public static boolean isEmpty(String str) {
        return str == null || str.equals("");
    }

    public static String appendLineSeperator(String msg) {
        return msg.concat(getLineSeperator());
    }

    public static String getLineSeperator() {
        return System.lineSeparator();
    }
}
