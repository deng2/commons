package com.greenbee.commons.test;

import java.text.ParseException;

import com.greenbee.commons.ReflectionLib;
import junit.framework.TestCase;

public class ReflectionLibTest extends TestCase {
    @SuppressWarnings("FieldCanBeLocal")
    public static class Test {
        public static String a = "a";
        public String b = "b";
        private String c = "c";

        public String getC() {
            return c;
        }

        public static void testParseException() throws ParseException {
            throw new ParseException("testException", 0);
        }
    }

    public void testInvokeStatic() throws Exception {
        String clazz = this.getClass().getName() + "$Test";
        ClassLoader cl = ReflectionLib.class.getClassLoader();
        try {
            ReflectionLib.invokeStatic(cl, clazz, "testParseException");
        } catch (ParseException ignored) {
            //succeed
        }
    }

    public void testGeneral() throws Exception {
        assertEquals("b", ReflectionLib.get(new Test(), "b"));
        assertEquals("c", ReflectionLib.getDeclared(new Test(), "c"));
        assertEquals("a", ReflectionLib.getStatic(Test.class, "a"));
    }
}
