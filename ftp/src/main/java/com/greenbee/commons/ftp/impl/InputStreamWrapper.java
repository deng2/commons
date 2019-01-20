package com.greenbee.commons.ftp.impl;

import com.greenbee.commons.ftp.ConnectionLib;
import org.apache.commons.net.ftp.FTPClient;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamWrapper extends InputStream {
    private FTPClient fc_;
    private InputStream deledage_;

    public InputStreamWrapper(FTPClient fc, InputStream is) {
        super();
        this.fc_ = fc;
        this.deledage_ = is;
    }

    public int read() throws IOException {
        return deledage_.read();
    }

    public int read(byte[] b) throws IOException {
        return deledage_.read(b);
    }

    public int read(byte[] b, int off, int len) throws IOException {
        return deledage_.read(b, off, len);
    }

    public long skip(long n) throws IOException {
        return deledage_.skip(n);
    }

    public int available() throws IOException {
        return deledage_.available();
    }

    public void mark(int readlimit) {
        deledage_.mark(readlimit);
    }

    public void reset() throws IOException {
        deledage_.reset();
    }

    public boolean markSupported() {
        return deledage_.markSupported();
    }

    public void close() throws IOException {
        if (deledage_ != null) {
            ConnectionLib.closeStream(deledage_);
            fc_.completePendingCommand();
            deledage_ = null;
        }
    }
}
