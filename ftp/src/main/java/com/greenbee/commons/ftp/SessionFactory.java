// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.ftp;

public interface SessionFactory {

    /**
     * open a session
     */
    public Session createSession() throws FTPException;

    /**
     * close session factory to free up resources
     */
    public void close();
}
