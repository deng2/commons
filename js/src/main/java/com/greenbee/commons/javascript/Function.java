package com.greenbee.commons.javascript;

public class Function {

    public static void println(Object obj) {
        obj = ScriptLib.unwrap(obj);
        System.out.println(obj);
    }

    public static void print(Object obj) {
        obj = ScriptLib.unwrap(obj);
        System.out.print(obj);
    }

}
