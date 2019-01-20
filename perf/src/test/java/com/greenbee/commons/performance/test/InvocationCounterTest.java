package com.greenbee.commons.performance.test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import com.greenbee.commons.performance.InvocationCounter;

public class InvocationCounterTest extends PerformanceTestBase {
    public void testLogRound() throws IOException {
        PrintStream systemStream = System.out;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(bos, true));
            //begin testing
            bos.reset();
            //round = 20 but invoke 19, so no line is printed
            invokePerformanceCounter(20, 19);
            String[] results = readLines(bos.toByteArray(), systemStream);
            assertEquals(0, results.length);

            //round = 20 and invoke 20, so one line is printed
            bos.reset();
            invokePerformanceCounter(20, 20);
            results = readLines(bos.toByteArray(), systemStream);
            assertEquals(1, results.length);

            //20 & 40
            bos.reset();
            invokePerformanceCounter(20, 40);
            results = readLines(bos.toByteArray(), systemStream);
            assertEquals(2, results.length);
        } finally {
            System.setOut(systemStream);
        }
    }

    protected void invokePerformanceCounter(int initRound, int invokeCount) {
        InvocationCounter pc = new InvocationCounter("Test", initRound);
        for (int i = 0; i < invokeCount; i++) {
            try {
                pc.start();
            } finally {
                pc.end();
            }
        }
    }

}
