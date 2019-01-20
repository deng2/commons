package com.greenbee.commons.ftp.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.PrintWriter;

public class LogPrintWriter extends PrintWriter {
    private static final Log log_ = LogFactory.getLog(LogPrintWriter.class);
    private StringBuilder sb_ = new StringBuilder();

    public LogPrintWriter() {
        super(System.out);
    }

    @Override public synchronized void print(String s) {
        if (log_.isDebugEnabled())
            sb_.append(s);
    }

    @Override public synchronized void println(String x) {
        if (log_.isDebugEnabled())
            sb_.append(x).append(System.lineSeparator());
    }

    @Override public synchronized void flush() {
        if (log_.isDebugEnabled()) {
            log_.debug("FTP command: " + System.lineSeparator() + sb_.toString().trim());
            sb_.delete(0, sb_.length());
        }
    }

}
