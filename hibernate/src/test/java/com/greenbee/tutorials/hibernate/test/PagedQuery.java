package com.greenbee.tutorials.hibernate.test;

import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;

import com.greenbee.commons.hibernate.SessionOperation;
import com.greenbee.tutorials.hibernate.HibernateUtil;

public class PagedQuery {
    public static void main(String[] args) {
        final Integer pos = (Integer) HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(Session session) throws Exception {
                Query query = session.createQuery("from Parent order by id");
                query.setFirstResult(0);
                query.setMaxResults(3);
                Iterator<?> itr = query.iterate();
                int i = iterate(itr);
                return i;
            }
        });

        HibernateUtil.execute(new SessionOperation() {

            @Override
            public Object operate(Session session) throws Exception {
                Query query = session.createQuery("from Parent order by id");
                query.setFirstResult(pos);
                Iterator<?> itr = query.iterate();
                iterate(itr);
                return null;
            }
        });
    }

    protected static void log(Object msg) {
        System.out.println(msg);
    }

    protected static int iterate(Iterator<?> itr) {
        log("Begin to iterate: ");
        int i = 0;
        while (itr.hasNext()) {
            log(itr.next());
            i++;
        }
        return i;
    }
}
