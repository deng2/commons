package com.greenbee.commons.test;

import org.apache.commons.logging.LogFactory;

import com.greenbee.commons.CommandLineLogger;
import com.greenbee.commons.DateLib;
import junit.framework.TestCase;

public class DateLibTest extends TestCase {
    public void testDateParse() throws Exception {
        try {
            String format = "dd/MM/yyyy HH:mm:ss.SSS";
            String date1 = "17/08/2018 14:17:59.597";
            CommandLineLogger.print(DateLib.parseDate(format, date1));
            String date2 = "17/08/2018 14:17:59";
            CommandLineLogger.print(DateLib.parseDate(format, date2));
        } catch (Exception e) {
            LogFactory.getLog(DateLibTest.class).error(e);
        }
    }
}
