package com.greenbee.commons.performance.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import com.greenbee.commons.performance.ProcessingCounter;

public class ProcessingCounterTest extends PerformanceTestBase {
    public void testLogRound() throws IOException {
        PrintStream systemStream = System.out;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(bos, true));
            //begin testing
            bos.reset();
            invokePerformanceClass(20, 19);
            String[] results = readLines(bos.toByteArray(), systemStream);
            assertEquals(0, results.length);

            bos.reset();
            invokePerformanceClass(20, 20);
            results = readLines(bos.toByteArray(), systemStream);
            assertEquals(1, results.length);

            bos.reset();
            invokePerformanceClass(20, 40);
            results = readLines(bos.toByteArray(), systemStream);
            assertEquals(2, results.length);

        } finally {
            System.setOut(systemStream);
        }
    }

    protected void invokePerformanceClass(int initRound, int invokeCount) {
        ProcessingCounter pk = new ProcessingCounter("PerformanceUnitTest", initRound);
        for (int i = 0; i < invokeCount; i++) {
            pk.count();
        }
    }
}
