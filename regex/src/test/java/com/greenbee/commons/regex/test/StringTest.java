package com.greenbee.commons.regex.test;

import junit.framework.TestCase;

public class StringTest extends TestCase {

    public void testSplit() {
        String str = "abc  def";
        String[] ss = str.split(" ");
        assertEquals(3, ss.length);
        assertEquals("abc", ss[0]);
        assertEquals("", ss[1]);
        assertEquals("def", ss[2]);
    }

}
