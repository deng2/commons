// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.ftp.impl;

import com.greenbee.commons.ftp.ConnectionInfo;
import com.greenbee.commons.ftp.FTPException;
import com.greenbee.commons.ftp.Session;
import com.greenbee.commons.ftp.SessionFactory;

public class SessionFactoryImpl implements SessionFactory {
    private ConnectionInfo info_;

    public SessionFactoryImpl(ConnectionInfo info) {
        info_ = info;
    }

    @Override public Session createSession() throws FTPException {
        return new SessionImpl(this);
    }

    @Override public void close() {
        //do nothing for now
    }

    protected ConnectionInfo getConnectionInfo() {
        return info_;
    }

}
