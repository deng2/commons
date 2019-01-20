// Copyright (c) 2013 by Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class SessionExecutor {
    private SessionFactory sf_;

    public SessionExecutor(SessionFactory sf) {
        this.sf_ = sf;
    }

    public <T> T execute(SessionOperation<T> so) throws Exception {
        Session session = null;
        Transaction transaction = null;
        try {
            Exception exception = null;
            session = sf_.openSession();
            transaction = session.getTransaction();
            transaction.begin();
            T result = null;
            try {
                result = so.operate(session);
            } catch (Exception e) {
                exception = e;
            }
            if (exception == null) {
                transaction.commit();
            } else {
                HibernateLib.rollback(transaction);
                throw exception;
            }
            return result;
        } finally {
            HibernateLib.close(session);
        }
    }

}
