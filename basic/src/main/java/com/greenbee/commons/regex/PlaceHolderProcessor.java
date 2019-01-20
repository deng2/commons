// Copyright (c) 2014 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.regex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlaceHolderProcessor {
    protected static final String DELIM_COLON = ":";
    private List<Object> contents_ = new ArrayList<Object>();
    private List<String> names_ = new ArrayList<String>();
    private Map<String, String> defaults = new HashMap<>();

    public PlaceHolderProcessor(String content, String... quotes) {
        int len = quotes.length;
        if (len % 2 != 0) {
            String[] aquotes = new String[len + 1];
            System.arraycopy(quotes, 0, aquotes, 0, len);
            aquotes[len] = "";
            quotes = aquotes;
            len = aquotes.length;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len / 2; i++) {
            if (i != 0)
                sb.append("|");
            String start = quotes[2 * i];
            String end = quotes[2 * i + 1];
            sb.append(Pattern.quote(start));
            sb.append("([\\w\\.:-]+)");
            if (end.length() > 0)
                sb.append(Pattern.quote(end));
        }
        String reg = sb.toString();
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(content);
        int lastEnd = 0;
        while (matcher.find()) {
            String name = null;
            for (int i = 1; i <= matcher.groupCount(); i++) {
                name = matcher.group(i);
                if (name != null)
                    break;
            }
            int index;
            if ((index = name.indexOf(DELIM_COLON)) >= 0) {
                String value = name.substring(index + 1);
                name = name.substring(0, index);
                defaults.put(name, value);
            }
            names_.add(name);
            int start = matcher.start();
            if (start != 0)
                contents_.add(content.substring(lastEnd, start));
            contents_.add(new PlaceHolderInfo(name, matcher.group()));
            lastEnd = matcher.end();
        }
        if (lastEnd != content.length())
            contents_.add(content.substring(lastEnd));

    }

    public List<String> getPlaceHolderNames() {
        return names_;
    }

    public String replace(Map<String, String> input) {
        Map<String, String> combined = new HashMap<>();
        combined.putAll(defaults);
        combined.putAll(input);
        input = combined;
        StringBuilder sb = new StringBuilder();
        for (Object obj : contents_) {
            if (obj instanceof PlaceHolderInfo) {
                PlaceHolderInfo info = (PlaceHolderInfo) obj;
                String key = info.getName();
                String value = input.get(key);
                if (value == null) {
                    sb.append(info.getOriginal());//don't replace
                } else {
                    sb.append(value);
                }
            } else if (obj instanceof String) {
                String content = (String) obj;
                sb.append(content);
            }
        }
        return sb.toString();
    }

    private static class PlaceHolderInfo {
        private String name_;
        private String orig_;

        private PlaceHolderInfo(String name, String orig) {
            super();
            this.name_ = name;
            orig_ = orig;
        }

        public String getName() {
            return name_;
        }

        public String getOriginal() {
            return orig_;
        }

    }
}
