// Copyright (c) 2014 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.performance;

public class EPSController {
    private static final int SLICE_COUNT = 100;
    private static final long MILLIS = 1000;
    private static final long NANOSECONDS = 1000000;
    //settings
    private int epso_ = -1;
    private long cycle_ = 60 * MILLIS * NANOSECONDS;
    private int batch_;
    private long duration_ = -1; //duration in milli seconds
    //status
    private int eps_ = -1;
    private long startTime_ = -1;
    private int count_ = 0;

    public EPSController(int eps) {
        this(eps, -1);
    }

    public EPSController(int eps, int duration) {
        if (eps > 0)
            epso_ = eps_ = eps;
        if (eps < SLICE_COUNT) {
            batch_ = eps;
        } else {
            batch_ = eps / SLICE_COUNT;
        }
        if (eps > 0 && duration > 0) {
            duration_ = duration * 1000;
        }
    }

    public static void main(String[] args) {
        int ep = 100;
        EPSController controller = new EPSController(ep, -1);
        ProcessingMonitor monitor = new ProcessingMonitor("t", 1);
        int round = 3;
        int count = 0;
        long start = System.currentTimeMillis();
        while (round > 0) {
            count++;
            controller.count();
            if (monitor.count())
                round--;
        }
        long duration = System.currentTimeMillis() - start;
        long eps = (count * 1000L / duration);
        System.out.println("EPS: " + eps + " count:" + count + " duration:" + duration);
    }

    public synchronized void count() {
        if (duration_ > 0) {
            long currentTime = System.currentTimeMillis();
            if (startTime_ > 0 && currentTime - startTime_ > duration_) {
                if (eps_ < 0)
                    eps_ = epso_;
                else
                    eps_ = -1;

                count_ = 0;
                startTime_ = System.currentTimeMillis();//for eps -1 case
            }
        }
        if (eps_ < 0)
            return;
        if (count_ == 0) {
            startTime_ = System.currentTimeMillis();
            count_++;
        } else {
            count_++;
            if (count_ % batch_ == 0) {
                long passedTime = (System.currentTimeMillis() - startTime_) * NANOSECONDS;
                long expectTime = MILLIS * NANOSECONDS * (count_ - 1) / eps_;
                if (passedTime < expectTime) {
                    long sleepTime = expectTime - passedTime;
                    long sleepMillis = sleepTime / NANOSECONDS;
                    int sleepNanos = (int) (sleepTime % NANOSECONDS);
                    try {
                        Thread.sleep(sleepMillis, sleepNanos);
                    } catch (InterruptedException ie) {
                        //ignore
                    }
                }
                if (passedTime > cycle_) {
                    count_ = 0;
                }
            }
        }
    }
}
