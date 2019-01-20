// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.sftp.impl;

import com.greenbee.commons.sftp.ConnectionInfo;
import com.greenbee.commons.sftp.SFTPException;
import com.greenbee.commons.sftp.Session;
import com.greenbee.commons.sftp.SessionFactory;

public class SessionFactoryImpl implements SessionFactory {
    private ConnectionInfo info_;

    public SessionFactoryImpl(ConnectionInfo info) {
        info_ = info;
    }

    @Override public Session createSession() throws SFTPException {
        return new SessionImpl(this);
    }

    @Override public void close() {
        //do nothing for now
    }

    protected ConnectionInfo getConnectionInfo() {
        return info_;
    }

}
