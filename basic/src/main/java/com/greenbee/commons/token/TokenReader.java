// Copyright (c) 2014 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.token;

import java.io.IOException;

public interface TokenReader {

    /**
     * return true if more token can be read
     * this method can be called multiple times without moving to next token before nextToken() API
     */
    public boolean hasMoreTokens() throws IOException;

    /**
     * move to the next token
     */
    public String nextToken() throws IOException;

    /**
     * the token start position in the file
     */
    public long getStartPosition();

    /**
     * the token end position in the file
     */
    public long getEndPosition();

    /**
     * clean buffer in the memory
     */
    public void reset();
}
