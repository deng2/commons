// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.regex.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GroupRegExpSample {
    /**
     * meta characters:
     *  ()[]{}.*?+
     */

    public static void main(String[] args) {
        String input = "http://stackoverflow.com/";
        String regex = "(?:http|ftp)://([^/\r\n]+)(/[^\r\n]*)?";
        print(regex, input);

        regex = "((\\w+)=(.+))";
        input = "ab=1234";
        print(regex, input);

        regex = "(\\w+)=\\1";
        input = "abc=abc";
        print(regex, input);

        regex = "\\$(\\w+)|\\$\\{(\\w+)\\}";
        print(regex, "$VTBA_HOME");
        print(regex, "${VTBA_HOME}");

        regex = "(\\\\%)|(\\\\_)|(%)|(_)";
        print(regex, "\\%");
        print(regex, "%");
        print(regex, "\\_");
        print(regex, "_");
    }

    private static void print(String regex, String input) {
        Matcher matcher = Pattern.compile(regex).matcher(input);
        if (matcher.matches()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= matcher.groupCount(); i++) {
                if (i != 1)
                    sb.append(" ");
                sb.append(i).append(":").append(matcher.group(i));
            }
            System.out.println(sb.toString());
        }
    }
}
