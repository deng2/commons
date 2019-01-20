// Copyright (c) 2014 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.token;

import java.io.IOException;

public class SingleLineTokenReader implements TokenReader {
    public static final int LINE_CAPACITY = 1024 * 1024;
    private StreamReader reader_;

    private String token_;
    private StringBuilder cache_;
    private long startPosition_;
    private long endPosition_;

    private boolean recordPosition_;
    private long[] positions_;
    private int tokenlength_ = -1;

    public SingleLineTokenReader(StreamReader reader) {
        this(reader, false);
    }

    public SingleLineTokenReader(StreamReader reader, boolean recordPosition) {
        reader_ = reader;
        reset();
        recordPosition_ = recordPosition;
        if (recordPosition_) {
            positions_ = new long[LINE_CAPACITY];
        }
    }

    public boolean hasMoreTokens() throws IOException {
        if (token_ == null)
            nextToken();
        return token_ != null;
    }

    public String nextToken() throws IOException {
        if (token_ != null) {
            String rtn = token_;
            token_ = null;
            return rtn;
        }
        if (startPosition_ == -1)
            startPosition_ = reader_.getPosition();
        if (cache_.length() == 0 && endPosition_ != -1)
            startPosition_ = endPosition_;
        boolean foundDelimiter = false;
        boolean dataDrained = false;
        while (true) {
            int ch = reader_.readChar();
            if (ch == -1) {
                if (reader_.reachEOF())
                    dataDrained = true;
                break;
            }
            if (ch == '\r' || ch == '\n') {
                foundDelimiter = true;
                break;
            }
            cache_.append((char) ch);
            if (recordPosition_) {
                positions_[cache_.length()] = reader_.getPosition();
            }
            if (cache_.length() > LINE_CAPACITY) {
                String msg = String.format("The line exceed line capacity size %s", LINE_CAPACITY);
                throw new IOException(msg);
            }
        }
        if (foundDelimiter || (dataDrained && cache_.length() > 0)) {
            endPosition_ = reader_.getPosition();
            token_ = cache_.toString();
            tokenlength_ = token_.length();
            cache_.delete(0, cache_.length());
        }
        return token_;
    }

    public long getPosition(int lineIndex) {
        if (tokenlength_ <= lineIndex)
            return getEndPosition();
        return positions_[lineIndex];
    }

    public long getStartPosition() {
        return startPosition_;
    }

    public long getEndPosition() {
        return endPosition_;
    }

    public void reset() {
        token_ = null;
        cache_ = new StringBuilder();

        startPosition_ = -1;
        endPosition_ = -1;
    }
}
