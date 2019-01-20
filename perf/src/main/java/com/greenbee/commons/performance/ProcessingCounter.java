package com.greenbee.commons.performance;

import com.greenbee.commons.CommandLineLogger;

/**
 *  calculate the average time between count() call
 *  this class is designed to calculate event processing speed 
 */
public class ProcessingCounter {
    protected static final String LOG_PREFIX = ProcessingCounter.class.getName();
    private final int round_;
    private final String identity_;
    private long startTime_;
    private int count_ = 0;

    public ProcessingCounter(String identity, int round) {
        this.round_ = round;
        this.identity_ = identity;
        if (round <= 1) {
            throw new IllegalArgumentException("Argument [round] is invalid");
        }
    }

    public static void main(String[] args) {
        //[Sleep-Average] average time: 500.0 (ms/event) EPS:2.0 (event/second)
        int round = 3;
        int logCount = 3;
        int sleep = 500;
        ProcessingCounter keep = new ProcessingCounter("Sleep-Average", round);
        for (int j = 0; j < logCount; j++) {
            try {
                Thread.sleep(sleep * round);
            } catch (InterruptedException ie) {
                //ignore
            }
            for (int i = 0; i < round; i++) {
                keep.count();
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException re) {

                }
            }
        }
    }

    public boolean count() {
        double averageTime = 0; //second/event
        boolean logFalg = false;
        synchronized (this) {
            if (count_ == 0) {
                startTime_ = System.currentTimeMillis();
                count_++;
            } else if (count_ == round_ - 1) {
                long spentTime = System.currentTimeMillis() - startTime_;
                int count = round_ - 1;
                count_ = 0;
                averageTime = ((double) spentTime) / count;
                logFalg = true;
            } else {
                count_++;
            }
        }
        if (logFalg == true) {
            double eps = 1000.0 / averageTime;
            log("[" + identity_ + "] average time: " + averageTime + " (ms/event) EPS:" + eps
                    + " (event/second)");
        }
        return logFalg;

    }

    protected void log(String msg) {
        CommandLineLogger.log(msg);
    }
}
