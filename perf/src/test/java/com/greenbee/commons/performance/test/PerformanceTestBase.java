package com.greenbee.commons.performance.test;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public abstract class PerformanceTestBase extends TestCase {

    protected String[] readLines(byte[] input, PrintStream systemStream) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(input);
        BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
        List<String> array = new ArrayList<String>();
        while (true) {
            String line = reader.readLine();
            if (line != null) {
                array.add(line);
            } else {
                break;
            }
        }
        systemStream.println("~~~~~~");
        for (String line : array) {
            systemStream.println(line);
        }
        return array.toArray(new String[0]);
    }

}