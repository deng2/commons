package com.greenbee.commons.regex.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KeyValueSample {
    public static String KEY_VALUE_REGEX = "(\\w+)\\=(.+)";

    public static void main(String[] args) {
        parse("abc=dddd");
    }

    public static void parse(String input) {
        Pattern pattern = Pattern.compile(KEY_VALUE_REGEX);
        Matcher macher = pattern.matcher(input);
        if (macher.matches()) {
            String key = macher.group(1);
            String value = macher.group(2);
            System.out.println("'" + key + "'_'" + value + "'");
        }
    }
}
