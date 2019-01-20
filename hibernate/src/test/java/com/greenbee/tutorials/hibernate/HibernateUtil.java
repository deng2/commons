package com.greenbee.tutorials.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.greenbee.commons.hibernate.SessionExecutor;
import com.greenbee.commons.hibernate.SessionOperation;

public class HibernateUtil {

    private static final SessionFactory sessionFactory_ = buildSessionFactory();
    private static final SessionExecutor executor_ = new SessionExecutor(sessionFactory_);

    private static SessionFactory buildSessionFactory() {
        try {
            return new Configuration().configure("h2.hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory_;
    }

    public static SessionExecutor getExecutor() {
        return executor_;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }

    public static Object execute(SessionOperation so) throws HibernateException {
        try {
            return executor_.execute(so);
        } catch (HibernateException he) {
            throw he;
        } catch (Exception e) {
            throw new HibernateException(e);
        }
    }

}
