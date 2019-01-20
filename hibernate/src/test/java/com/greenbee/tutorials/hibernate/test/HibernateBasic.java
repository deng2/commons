package com.greenbee.tutorials.hibernate.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.greenbee.commons.hibernate.SessionOperation;
import com.greenbee.tutorials.hibernate.HibernateUtil;
import com.greenbee.tutorials.hibernate.model.ConnectorState;

import junit.framework.TestCase;

public class HibernateBasic extends TestCase {
    private static final Log log_ = LogFactory.getLog(HibernateBasic.class);

    public void testBasic() throws Exception {
        //delete when it exists
        HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(org.hibernate.Session session) throws Exception {
                ConnectorState cs = (ConnectorState) session.get(ConnectorState.class, 1L);
                if (cs != null)
                    session.delete(cs);
                return null;
            }
        });

        HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(org.hibernate.Session session) throws Exception {
                ConnectorState cs = new ConnectorState();
                cs.setConnectorId(1L);
                cs.setInstanceId(1);
                cs.setComponentFullName("TEST");
                cs.setState("Hello".getBytes());

                session.save(cs);
                log_.info(cs);
                log_.info(cs == session.get(ConnectorState.class, 1L));//same object

                return null;
            }
        });

        HibernateUtil.execute(new SessionOperation() {

            @Override
            public Object operate(org.hibernate.Session session) throws Exception {
                ConnectorState cs = new ConnectorState();
                cs.setConnectorId(1L);
                cs.setInstanceId(1);
                cs.setComponentFullName("TEST_UPDATE");
                cs.setState("Hello".getBytes());

                session.update(cs);
                return null;
            }

        });

    }

}
