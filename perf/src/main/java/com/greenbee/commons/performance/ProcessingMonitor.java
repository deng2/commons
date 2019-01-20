package com.greenbee.commons.performance;

import com.greenbee.commons.CommandLineLogger;

/**
 *  calculate the average time between count() call in a specific time
 *  this class is designed to calculate event processing speed 
 */
public class ProcessingMonitor {
    //protected static final String LOG_PREFIX = EventProcessingPerformanceMonitor.class.getName();

    private final String identity_;
    private final long monitorTime_; //in millisecond
    private long startTime_;
    private int count_ = 0; //how many event processed in the time period

    public ProcessingMonitor(String identity, long seconds) {
        this.identity_ = identity;
        this.monitorTime_ = seconds * 1000;
        if (seconds <= 0) {
            throw new IllegalArgumentException("Argument [seconds] is invalid");
        }
    }

    public static void main(String[] args) {
        int ms = 1;
        int sleep = 1500;
        int loop = ((ms * 1000) / sleep + 2);
        int logCount = 4;
        //[average-30] time period: 2.001 (s) average time: 2001.0 (ms/event) EPS: 0.49975012493753124 (event/second)
        ProcessingMonitor em = new ProcessingMonitor("average-30", ms);

        for (int j = 0; j < logCount; j++) {
            try {
                Thread.sleep(sleep);
            } catch (InterruptedException ie) {
                //ignore
            }
            for (int i = 0; i < loop; i++) {
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException ie) {

                }
                em.count();
            }
        }
    }

    public boolean count() {
        long timeMillis = 0;
        long count = 0;
        boolean flagLog = false;
        synchronized (this) {
            if (count_ == 0) {
                startTime_ = System.currentTimeMillis();
                count_++;
            } else {
                count_++;

                long passedTime = System.currentTimeMillis() - startTime_;
                if (passedTime > monitorTime_) {
                    timeMillis = passedTime;
                    count = count_ - 1;
                    count_ = 0;
                    flagLog = true;
                }
            }
        }

        if (flagLog) {
            double averageMillis = ((double) timeMillis) / count;
            double eps = 1000.0 / averageMillis;
            log("[" + identity_ + "] time period: " + timeMillis / 1000.0 + " (s) average time: "
                    + averageMillis + " (ms/event) EPS: " + eps + " (event/second)");
        }
        return flagLog;
    }

    protected void log(String msg) {
        CommandLineLogger.log(msg);
    }
}
