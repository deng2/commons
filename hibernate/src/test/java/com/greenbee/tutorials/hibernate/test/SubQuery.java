package com.greenbee.tutorials.hibernate.test;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

import com.greenbee.commons.hibernate.SessionOperation;
import com.greenbee.tutorials.hibernate.HibernateUtil;
import com.greenbee.tutorials.hibernate.model.mappig.Parent;
import com.greenbee.tutorials.hibernate.model.mappig.Child;

import junit.framework.TestCase;

public class SubQuery extends TestCase {
    public static void main(String[] args) {
        SubQuery main = new SubQuery();
        main.testSubQuery();
        main.testSubQuery2();
    }

    public void testSubQuery() {
        HibernateUtil.execute(new SessionOperation() {
            @SuppressWarnings("rawtypes")
            @Override
            public Object operate(Session session) throws Exception {
                Query query = session.createQuery(
                        "from Parent as a where exists (from Child as b where b.parent = a)");
                List list = query.list();
                for (Object obj : list) {
                    Parent parent = (Parent) obj;
                    log(parent);
                }
                return null;
            }
        });
    }

    public void testSubQuery3() {
        HibernateUtil.execute(new SessionOperation() {
            @SuppressWarnings("rawtypes")
            @Override
            public Object operate(Session session) throws Exception {
                Criteria criteria = session.createCriteria(Parent.class, "a");
                DetachedCriteria dc = DetachedCriteria.forClass(Child.class, "b");
                dc.add(Restrictions.eqProperty("b.parent.id", "a.id"));
                dc.setProjection(Projections.id());
                criteria.add(Subqueries.exists(dc));
                List list = criteria.list();
                for (Object obj : list) {
                    Parent parent = (Parent) obj;
                    log(parent);
                }
                return null;
            }
        });
    }

    public void testSubQuery2() {
        HibernateUtil.execute(new SessionOperation() {
            @SuppressWarnings("rawtypes")
            @Override
            public Object operate(Session session) throws Exception {
                Query query = session
                        .createQuery("from Parent as a where exists elements(a.children)");
                List list = query.list();
                for (Object obj : list) {
                    Parent parent = (Parent) obj;
                    log(parent);
                }
                return null;
            }
        });
    }

    protected static void log(Object msg) {
        System.out.println(msg);
    }
}
