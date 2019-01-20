// Copyright (c) 2013 by Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HibernateLib {

    public static void close(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (HibernateException he) {
                //ignore
            }
        }
    }

    public static void rollback(Transaction tran) {
        if (tran == null)
            return;
        try {
            tran.rollback();
        } catch (HibernateException he) {
            //ignore
        }
    }
}
