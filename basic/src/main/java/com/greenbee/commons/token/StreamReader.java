// Copyright (c) 2014 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.token;

import com.greenbee.commons.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

public class StreamReader {
    private Charset charset_;
    private CharsetDecoder decoder_;

    private ByteBuffer bb_;
    private boolean eof_;
    //source read position
    private long position_;
    private CharBuffer cb_ = CharBuffer.allocate(1);
    //buffer for UNICODE Supplementary Characters 
    private CharBuffer uscb_ = CharBuffer.allocate(2);

    public StreamReader(String encoding, long position, int bs) {
        if (StringUtils.isEmpty(encoding)) {
            charset_ = Charset.defaultCharset();
        } else {
            charset_ = Charset.forName(encoding);
        }
        decoder_ = charset_.newDecoder();
        decoder_.onMalformedInput(CodingErrorAction.REPLACE);
        decoder_.onUnmappableCharacter(CodingErrorAction.REPLACE);

        bb_ = ByteBuffer.allocateDirect(bs);
        flip();
        position_ = position;
        eof_ = false;
    }

    /**
     * @param is: the input stream. in append mode, the input stream should be moved to the offset
     * @param append: if true, append content to the stream reader
     * write stream
     */
    public void writeStream(InputStream is, boolean append) throws IOException {
        writeStream(is, append, 0);
    }

    public void writeStream(InputStream is, long skip) throws IOException {
        writeStream(is, false, skip);
    }

    private void writeStream(InputStream is, boolean append, long skip) throws IOException {
        if (append) {
            position_ += bb_.position();
            compact();
        } else {
            position_ = skip; //start from the offset
            is.skip(skip);
            clear();
        }
        byte[] buf = new byte[bb_.remaining()];
        int len = is.read(buf);
        if (len <= 0)
            eof_ = true;
        else {
            bb_.put(buf, 0, len);
            eof_ = false;
        }
        flip();

        if (!append) {
            decoder_.reset();
        }
    }

    //if append, read from reader recorded position    
    //else read from source specified position
    public void writeStream(FileChannel source, boolean isLiveFile, boolean append)
            throws IOException {
        if (append) {
            position_ += bb_.position();
            source.position(position_ + bb_.remaining());
            compact();
        } else {
            position_ = 0;
            source.position(position_);
            clear();
        }
        source.read(bb_);
        flip();
        if (isLiveFile) {
            eof_ = false;
        } else {
            //if end of the file
            eof_ = source.position() >= source.size();
        }
        if (!append) {
            decoder_.reset();
        }
    }

    public void writeStream(byte[] contents, boolean append) {
        long pos;
        if (append) {
            position_ += bb_.position();
            pos = position_ + bb_.remaining();
            compact();
        } else {
            position_ = 0;
            pos = 0;
            clear();
        }
        while (bb_.remaining() > 0 && pos < contents.length) {
            bb_.put(contents[(int) pos]);
            pos++;
        }
        flip();
        eof_ = pos >= contents.length;
        if (!append)
            decoder_.reset();
    }

    /***
     * clean buffer underneath
     */
    public void reset() {
        clear();
        flip();
        position_ = 0;
        eof_ = false;
    }

    /**
     * @return -1 means stream is drained. 
     */
    public int readChar() {
        if (uscb_.hasRemaining())
            return uscb_.get();
        if (!bb_.hasRemaining())
            return -1;
        cb_.clear();
        CoderResult cr = decoder_.decode(bb_, cb_, eof_);
        cb_.flip();
        if (cr.isOverflow() && !cb_.hasRemaining()) {
            //meet supplementary character
            uscb_.clear();
            decoder_.decode(bb_, uscb_, eof_);
            uscb_.flip();
            if (uscb_.hasRemaining())
                return uscb_.get();
        }
        int ch = -1;
        if (cb_.hasRemaining()) {
            ch = cb_.get();
        }
        return ch;
    }

    /**
     * @return the position of file if the stream is read from file
     */
    public long getPosition() {
        return position_ + bb_.position();
    }

    /**
     * @return true if it reach to the end of stream
     */
    public boolean reachEOF() {
        return eof_;
    }

    private void flip() {
        bb_.flip();
        uscb_.flip();
    }

    private void clear() {
        bb_.clear();
        uscb_.clear();
    }

    private void compact() {
        bb_.compact();
        uscb_.compact();
    }
}
