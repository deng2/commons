package com.greenbee.commons;

public class CommandLineLogger {
    //protected static boolean firstTimeFlag_ = true;

    public synchronized static void print(Object msg) {
        System.out.println(msg);
    }

    public synchronized static void log(Object msg) {
        System.out.println("[" + DateLib.getLogTimeStamp() + "] " + msg);
    }
}
