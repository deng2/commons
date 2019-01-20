// Copyright (c) 2017 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.regex.test;

import java.util.regex.Matcher;

import com.greenbee.commons.regex.handler.MatchHandler;
import com.greenbee.commons.regex.handler.RegexpParser;
import com.greenbee.commons.regex.handler.MismatchHandler;

public class SQLike2 {
    public static void main(String[] args) {
        //%: .* 
        //_: .
        String str = "_%a";
        System.out.println(like(str, "%") == true);
        System.out.println(like(str, "___") == true);
        System.out.println(like(str, "\\_\\%a") == true);
        System.out.println(like(str, "_") == false);
        System.out.println(like(str, "%a") == true);
    }

    public static boolean like(String col, String exp) {
        final StringBuilder sb = new StringBuilder();
        RegexpParser parser = new RegexpParser("(\\\\%)|(\\\\_)|(%)|(_)");
        parser.match(new MatchHandler() {
            @Override
            public void match(Matcher match) {
                if (match.group(1) != null)
                    sb.append("%");
                if (match.group(2) != null)
                    sb.append("_");
                if (match.group(3) != null)
                    sb.append(".*");
                if (match.group(4) != null)
                    sb.append(".");
            }
        }).mismatch(new MismatchHandler() {
            @Override
            public void mismatch(String rs) {
                sb.append(rs);
            }
        }).parse(exp);
        String reg = sb.toString();
        return col.matches(reg);
    }
}
