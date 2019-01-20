package com.greenbee.commons;

public class ThreadLib {
    /**
     * @param seconds - seconds to sleep
     */

    public static void sleep(int seconds) {
        if (seconds > 0) {
            try {
                Thread.sleep(((long) seconds) * 1000);
            } catch (InterruptedException ie) {
                //ignore
            }
        }
    }

}
