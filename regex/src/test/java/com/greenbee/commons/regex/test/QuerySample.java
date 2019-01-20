package com.greenbee.commons.regex.test;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuerySample {
    protected static String QUERY_STRING = "select id from vt_student where class=$$input1$$ and grade=$$input2$$ and name=$$input1$$";
    protected static String QUERY_REGEX = "\\$\\$(\\w+)\\$\\$";

    public static void main(String[] args) {
        HashMap<String, String> paramValues = new HashMap<String, String>();
        paramValues.put("input1", "1");
        paramValues.put("input2", "2");

        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile(QUERY_REGEX);
        Matcher matcher = pattern.matcher(QUERY_STRING);

        while (matcher.find()) {
            String key = matcher.group(1);
            matcher.appendReplacement(sb, paramValues.get(key));
        }
        matcher.appendTail(sb);

        System.out.println(sb.toString());
    }
}
