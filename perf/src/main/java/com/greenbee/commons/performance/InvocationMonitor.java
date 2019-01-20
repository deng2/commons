package com.greenbee.commons.performance;

import com.greenbee.commons.CommandLineLogger;

/**
 * calculate the average time spent between start() and end() API call
 * this class is designed for counting API performance
 */
public class InvocationMonitor {
    protected static final String LOG_PREFIX = InvocationMonitor.class.getName();
    private static final long NANOSECONDS = 1000000;
    private static final long MILLIS = 1000;
    private final String identity_;
    private final int eventsInRound_;
    private int count_ = 0;
    private long time_ = 0;
    private long roundTime_;
    private ThreadLocal<Long> startRecorder_ = new ThreadLocal<Long>();

    public InvocationMonitor(String ident, int roundTime) {
        this(ident, roundTime, 1);
    }

    private InvocationMonitor(String ident, int roundTime, int eventsInRound) {
        identity_ = ident;
        roundTime_ = roundTime * MILLIS * NANOSECONDS;
        eventsInRound_ = eventsInRound;
    }

    public static void main(String[] args) {
        int roundTime = 10;
        int sleepMillis = 1;

        //[Sleep] event processed: 30 (events) average time:100.53333333333333 (ms/event)
        InvocationMonitor sleep = new InvocationMonitor("Sleep", roundTime, 1);
        InvocationMonitor loop = new InvocationMonitor("Loop", roundTime,
                (int) (roundTime * MILLIS / sleepMillis));

        try {
            loop.start();
            while (true) {
                try {
                    sleep.start();
                    try {
                        Thread.sleep(sleepMillis);
                    } catch (InterruptedException ie) {

                    }
                } finally {
                    if (sleep.end())
                        break;
                }
            }
        } finally {
            loop.end();
        }
    }

    //thread safe to recording the start time of every thread
    public void start() {
        Long start = startRecorder_.get();
        if (start == null) {
            startRecorder_.set(System.nanoTime());
        } else {
            //throw new IllegalStateException("already start");
            log("[warning] thread " + Thread.currentThread().getName() + " already start");
        }
    }

    //thread safe
    public boolean end() {
        Long start = startRecorder_.get();
        if (start == null) {
            //throw new IllegalStateException("not started");
            log("[warning] thread " + Thread.currentThread().getName() + " has not been started");
            return false;
        } else {
            startRecorder_.remove();
            long time = System.nanoTime() - start.longValue();
            return count(time);
        }
    }

    protected boolean count(long timeInterval) {
        long time;
        int count;
        synchronized (this) {
            count_++;
            time_ += timeInterval;

            time = time_;
            count = count_;

            if (time_ >= roundTime_) {
                count_ = 0;
                time_ = 0;
            }
        }

        if (time >= roundTime_) {
            log("[" + identity_ + "] event processed: " + (count * eventsInRound_)
                    + " (events) average time:" + (double) time / NANOSECONDS / (count
                    * eventsInRound_) + " (ms/event)");
            return true;
        }
        return false;
    }

    protected void log(String msg) {
        CommandLineLogger.log(msg);
    }

}
