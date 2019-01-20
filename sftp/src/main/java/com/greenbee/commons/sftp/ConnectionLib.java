// Copyright (c) 2015 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.sftp;

import com.greenbee.commons.sftp.impl.SessionFactoryImpl;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectionLib {

    /**
     * method to create session factory
     */
    public static SessionFactory getSessionFactory(ConnectionInfo info) throws SFTPException {
        return new SessionFactoryImpl(info);
    }

    public static void closeStream(InputStream is) {
        closeIO(is);
    }

    public static void closeStream(OutputStream os) {
        closeIO(os);
    }

    private static void closeIO(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException ioe) {
                //ignore
            }
        }

    }

    public static void closeSession(Session session) {
        if (session != null) {
            session.close();
        }
    }

    public static void closeSessionFactory(SessionFactory sf) {
        if (sf != null) {
            sf.close();
        }

    }
}
