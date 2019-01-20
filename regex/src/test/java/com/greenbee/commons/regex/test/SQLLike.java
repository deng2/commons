// Copyright (c) 2017 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.regex.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLLike {
    public static void main(String[] args) {
        //%: .* 
        //_: .
        String str = "_%a";
        System.out.println(like(str, "%") == true);
        System.out.println(like(str, "___") == true);
        System.out.println(like(str, "\\_\\%a") == true);
        System.out.println(like(str, "_") == false);
        System.out.println(like(str, "%a") == true);

        System.out.println("\\aba".replaceAll("a|\\\\a", "t"));
    }

    public static boolean like(String col, String exp) {
        StringBuffer buffer = new StringBuffer();
        Pattern pattern = Pattern.compile("(\\\\%)|(\\\\_)|(%)|(_)");
        Matcher matcher = pattern.matcher(exp);
        int lastMatch = 0;
        while (matcher.find()) {
            String value;
            if (matcher.group(1) != null)
                value = "%";
            else if (matcher.group(2) != null)
                value = "_";
            else if (matcher.group(3) != null)
                value = ".*";
            else
                value = ".";
            String between = exp.substring(lastMatch, matcher.start());
            if (between.length() > 0)
                buffer.append(Pattern.quote(between));
            buffer.append(value);
            lastMatch = matcher.end();
        }
        String between = exp.substring(lastMatch);
        if (between.length() > 0)
            buffer.append(Pattern.quote(between));
        String reg = buffer.toString();
        return col.matches(reg);
    }
}
