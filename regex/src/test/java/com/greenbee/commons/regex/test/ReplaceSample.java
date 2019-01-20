package com.greenbee.commons.regex.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReplaceSample {
    public static String REPLACEMENT_REGEX = "\\$(\\w+)\\$";

    public static void main(String[] args) {
        replace("select a, b from t where a = $aa$ and b= $bb$");
        replace3();
        replace2();
    }

    private static void replace3() {
        String dateValue = "Thu Dec 17 00:00:00 GMT+0800 2015";
        removeTimeZone(dateValue);
        dateValue = "Thu Dec 17 00:00:00 GMT-0800 2015";
        removeTimeZone(dateValue);
    }

    private static void replace2() {
        String str = "\\";
        System.out.println(str.replaceAll("\\\\", Matcher.quoteReplacement("\\\\")));
    }

    protected static void removeTimeZone(String dateValue) {
        dateValue = dateValue.replaceAll(" GMT[^ ]* ", " ");
        System.out.println(dateValue);
    }

    /**
     * $1 is similar as \1 in VIM sub expression: %s:${\(abc\)}:%\1%:g
     */
    public static void replace(String input) {
        Pattern pattern = Pattern.compile(REPLACEMENT_REGEX);
        Matcher matcher = pattern.matcher(input);
        String result = matcher.replaceAll("$1");
        System.out.println(result);
    }
}
