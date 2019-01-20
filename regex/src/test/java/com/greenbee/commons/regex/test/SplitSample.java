package com.greenbee.commons.regex.test;

import java.util.regex.Pattern;

public class SplitSample {
    public static String SPLITER_REGEX = "\\?";

    public static void main(String[] args) {
        parse("?a?b?c? ?");
    }

    public static void parse(String input) {
        Pattern pattern = Pattern.compile(SPLITER_REGEX);
        String[] spliters = pattern.split(input);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < spliters.length; i++) {
            if (i != 0)
                sb.append("-");
            sb.append("'" + spliters[i] + "'");
        }

        System.out.println(sb.toString());
    }
}
