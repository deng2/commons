package com.greenbee.tutorials.hibernate.test;

import com.greenbee.commons.hibernate.SessionOperation;
import com.greenbee.tutorials.hibernate.HibernateUtil;
import com.greenbee.tutorials.hibernate.model.mappig.Parent;
import junit.framework.TestCase;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

public class JoinQuery extends TestCase {

    public void testJoin() throws Exception {
        HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(Session session) throws Exception {
                String hql = "select c from Parent p left join p.children c";
                Query query = session.createQuery(hql);
                List list = query.list();
                System.out.println(list);
                return null;
            }
        });
    }

    public void testJoin2() throws Exception {
        HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(Session session) throws Exception {
                Criteria query = session.createCriteria(Parent.class, "p");
                query.createCriteria("p.children", "c");
                List list = query.list();
                System.out.println(list);
                return null;
            }
        });
    }

    public void testCrossJoin() throws Exception {
        HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(Session session) throws Exception {
                String hql = "select distinct(p) from Parent p, Child c where p = c.parent";
                Query query = session.createQuery(hql);
                List list = query.list();
                System.out.println(list);
                return null;
            }
        });
    }

}
