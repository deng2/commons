// Copyright (c) 2017 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.regex.handler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexpParser {
    private String regexp_;
    private MatchHandler match_ = null;
    private MismatchHandler unmatch_ = null;

    public RegexpParser(String regexp) {
        super();
        this.regexp_ = regexp;
    }

    public RegexpParser match(MatchHandler handler) {
        match_ = handler;
        return this;
    }

    public RegexpParser mismatch(MismatchHandler handler) {
        unmatch_ = handler;
        return this;
    }

    public void parse(String input) {
        Pattern pattern = Pattern.compile(regexp_);
        Matcher matcher = pattern.matcher(input);
        int lastMatch = 0;
        while (matcher.find()) {
            int start = matcher.start();
            if (start > lastMatch) {
                String rs = input.substring(lastMatch, start);
                handleUnmatch(rs);
            }
            handleMatch(matcher);
            lastMatch = matcher.end();
        }
        if (lastMatch < input.length()) {
            handleUnmatch(input.substring(lastMatch));
        }
    }

    private void handleUnmatch(String rs) {
        if (unmatch_ != null) {
            unmatch_.mismatch(rs);
        }
    }

    private void handleMatch(Matcher matcher) {
        if (match_ != null)
            match_.match(matcher);
    }
}
