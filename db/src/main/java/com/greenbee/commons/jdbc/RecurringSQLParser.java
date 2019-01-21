// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecurringSQLParser {
    private static final String PLACEHOLDER_REG = "\\$\\$([\\-\\w]+)\\$\\$";

    private List<String> parameters = new ArrayList<String>();
    private String preparedSQL;

    public RecurringSQLParser(String sql) {
        StringBuilder sb = new StringBuilder();
        Pattern pattern = Pattern.compile(PLACEHOLDER_REG);
        Matcher matcher = pattern.matcher(sql);
        int lastEnd = 0;
        while (matcher.find()) {
            String name = matcher.group(1);
            parameters.add(name);
            int start = matcher.start();
            if (start != 0)
                sb.append(sql.substring(lastEnd, start));
            sb.append("?");
            lastEnd = matcher.end();
        }
        if (lastEnd != sql.length())
            sb.append(sql.substring(lastEnd));
        preparedSQL = sb.toString();
    }

    public String getPreparedSQL() {
        return preparedSQL;
    }

    public List<String> getVariableNames() {
        return parameters;
    }
}
