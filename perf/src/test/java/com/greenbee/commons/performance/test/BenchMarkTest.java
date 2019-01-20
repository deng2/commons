// Copyright (c) 2014 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.performance.test;

import com.greenbee.commons.performance.ProcessingCounter;
import com.greenbee.commons.performance.ProcessingMonitor;

public class BenchMarkTest {
    public static void main(String[] args) {
        int round = 10000000 * 10;
        ProcessingCounter counter = new ProcessingCounter("count", round);
        for (int i = 0; i < round * 4; i++) {
            counter.count();
            System.currentTimeMillis();
        }

        ProcessingMonitor monitor = new ProcessingMonitor("monitor", 10);
        int count = 0;
        while (count < 4) {
            System.currentTimeMillis();
            if (monitor.count())
                count++;
        }
        //EPS: 1.1678150064229826E7
        //EPS: 1.2228689197284345E7
    }
}
