package com.greenbee.commons.regex.test;

import java.util.regex.Pattern;

import junit.framework.TestCase;

public class MatchTest extends TestCase {

    public void testMatch1() throws Exception {
        //match all
        assertFalse(Pattern.matches("abc", "abcd"));
    }

}
