package com.greenbee.commons.performance;

import com.greenbee.commons.CommandLineLogger;

/**
 * calculate the average time spent between start() and end() API call
 * this class is designed for counting API performance
 */
public class InvocationCounter {
    protected static final String LOG_PREFIX = InvocationCounter.class.getName();
    protected static final long NANOSECONDS = 1000000;
    protected static final long MILLIS = 1000;
    private final String identity_;
    private final int round_; // after invoking start() and end(), we call it a round
    private final int eventsInRound_;
    private int count_ = 0;
    private long time_ = 0;
    private ThreadLocal<Long> startRecorder_ = new ThreadLocal<Long>();

    public InvocationCounter(String ident, int round) {
        this(ident, round, 1);
    }

    public InvocationCounter(String ident, int round, int eventsInRound) {
        identity_ = ident;
        round_ = round;
        eventsInRound_ = eventsInRound;
    }

    public static void main(String[] args) {
        int round = 5;
        int eventsInRound = 2;
        int sleepm = 200;

        //[Sleep] event processed: 30 (events) average time:100.53333333333333 (ms/event)
        InvocationCounter sleep = new InvocationCounter("Sleep", round, eventsInRound);
        InvocationCounter loop = new InvocationCounter("Loop-Sleep", 1, round * eventsInRound);
        try {
            loop.start();
            for (int i = 0; i < round; i++) {
                try {
                    sleep.start();
                    for (int j = 0; j < eventsInRound; j++) {
                        try {
                            Thread.sleep(sleepm);
                        } catch (InterruptedException ie) {

                        }
                    }
                } finally {
                    sleep.end();
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

            if (count_ == round_) {
                count_ = 0;
                time_ = 0;
            }
        }

        if (count % round_ == 0) {
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
