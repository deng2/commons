// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.sftp;

public interface SessionFactory {

    /**
     * open a session
     */
    public Session createSession() throws SFTPException;

    /**
     * close session factory to free up resources
     */
    public void close();
}
