package com.greenbee.commons.regex.test;

import java.util.regex.Pattern;

public class MatchSample {
    public static void main(String[] args) {
        matchIgnoreCase();
    }

    private static void matchIgnoreCase() {
        System.out.println(Pattern.matches("(?i:ab+c)", "ABBC"));
    }
}
