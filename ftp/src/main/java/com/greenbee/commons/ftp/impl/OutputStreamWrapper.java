package com.greenbee.commons.ftp.impl;

import com.greenbee.commons.ftp.ConnectionLib;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamWrapper extends OutputStream {
    private OutputStream deledage_;
    private FTPClient fc_;

    public OutputStreamWrapper(FTPClient fc, OutputStream deledage) {
        super();
        this.deledage_ = deledage;
        fc_ = fc;
    }

    public void write(int b) throws IOException {
        deledage_.write(b);
    }

    public void write(byte[] b) throws IOException {
        deledage_.write(b);
    }

    public void write(byte[] b, int off, int len) throws IOException {
        deledage_.write(b, off, len);
    }

    public void flush() throws IOException {
        deledage_.flush();
    }

    public void close() throws IOException {
        if (deledage_ != null) {
            ConnectionLib.closeStream(deledage_);
            fc_.completePendingCommand();
            deledage_ = null;
        }
    }
}
