package com.greenbee.commons.jms;

import com.greenbee.commons.StringUtils;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnectionFactory;
import javax.naming.Context;
import java.util.Properties;

public class ConnectionLib {
    private static final String JMS_CONNECTION_FACTORY_NAME = "connectionFactory";
    private static final String WMQ_VITRIA_JNDI_FACTORY = "com.vitria.jndi.wmq.WMQInitialContextFactory";
    private static final String AMQP_VITRIA_JNDI_FACTORY = "com.vitria.jndi.qpid.amqp_1_0.NamingContextFactory";
    private static final String HORNETQ_VITRIA_JNDI_FACTORY = "com.vitria.jndi.hornetq.HornetQInitialContextFactory";
    private static final String ACTIVEMQ_JNDI_FACTORY = "org.apache.activemq.jndi.ActiveMQInitialContextFactory";

    public static void enrichJndiEnv(Properties env, BaseInfo info) {
        String jndiFactory = env.getProperty(Context.INITIAL_CONTEXT_FACTORY);
        if (ACTIVEMQ_JNDI_FACTORY.equals(jndiFactory)) {
            enrichDestJndi(env, info);
        }

        if (HORNETQ_VITRIA_JNDI_FACTORY.equals(jndiFactory)) {
            enrichDestJndi(env, info);
            //connection factory
            env.put(JMS_CONNECTION_FACTORY_NAME, info.getConnectionFactoryName());
        }

        if (AMQP_VITRIA_JNDI_FACTORY.equals(jndiFactory)) {
            enrichDestJndi(env, info);
            env.put(JMS_CONNECTION_FACTORY_NAME, info.getConnectionFactoryName());
        }

        if (WMQ_VITRIA_JNDI_FACTORY.equals(jndiFactory)) {
            enrichDestJndi(env, info);
            enrichConnectionFactoryJNDI(env, info);

        }

    }

    public static void enrichSubInfo(Properties env, SubInfo subInfo) {
        String jndiFactory = env.getProperty(Context.INITIAL_CONTEXT_FACTORY);
        if (StringUtils.isEmpty(jndiFactory) || jndiFactory.startsWith("org.jnp")) {
            subInfo.setClientId("M3O");
        } else {
            enrichSubInfo(subInfo);
        }
    }

    public static void enrichSubInfo(SubInfo subInfo) {
        if (subInfo.isDurable() && StringUtils
                .isEmpty(subInfo.getClientId())) {//make the client id unique
            String dn = subInfo.getDestinationName();
            String sid = subInfo.getSubscriptionId();
            subInfo.setClientId("VOI_" + dn + "_" + sid);
        }
    }

    protected static void enrichConnectionFactoryJNDI(Properties env, BaseInfo info) {
        String key = info.isTopic() ? "topicConnectionFactory" : "queueConnectionFactory";
        key = key + "." + info.getConnectionFactoryName();
        String value = info.getConnectionFactoryName();
        env.put(key, value);
    }

    protected static void enrichDestJndi(Properties env, BaseInfo info) {
        String key = info.isTopic() ? "topic" : "queue";
        key = key + "." + info.getDestinationName();
        String value = info.getDestinationName();
        env.put(key, value);
    }

    public static ConnectionFactory getConnectionFactory(Context context, BaseInfo info)
            throws Exception {
        Object cf = context.lookup(info.getConnectionFactoryName());
        //throw exception when invalid
        validateConnectionFactory(cf, info);
        return (ConnectionFactory) cf;
    }

    public static Connection createConnection(ConnectionFactory factory, BaseInfo info,
                                              ExceptionListener el) throws Exception {
        Connection conn;
        String user = info.getUser();
        String password = info.getPassword();
        if (StringUtils.isEmpty(user) || StringUtils.isEmpty(password)) {
            conn = factory.createConnection();
        } else {
            conn = factory.createConnection(user, password);
        }
        try {
            if (info instanceof SubInfo) {
                SubInfo sinfo = (SubInfo) info;
                if (sinfo.isTopic() && sinfo.isDurable())
                    conn.setClientID(sinfo.getClientId());
            }
            if (el != null) {
                conn.setExceptionListener(el);
            }
            return conn;
        } catch (Exception e) {
            closeConnection(conn);
            throw e;
        }
    }

    public static Connection createConnection(ConnectionFactory factory, BaseInfo info)
            throws Exception {
        return createConnection(factory, info, null);
    }

    public static Session createSession(Connection conn, BaseInfo info) throws Exception {
        Session session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        return session;
    }

    public static Session createTransactedSession(Connection conn, BaseInfo info) throws Exception {
        Session session = conn.createSession(true, Session.SESSION_TRANSACTED);
        return session;

    }

    public static MessageConsumer createMessageConsumer(Context context, Session session,
            SubInfo subInfo) throws Exception {
        Object dest = context.lookup(subInfo.getDestinationName());
        validateDestination(dest, subInfo);
        Destination destination = (Destination) dest;
        MessageConsumer consumer;
        String selector = subInfo.getMessageSelector();
        if (subInfo.isTopic()) {
            if (subInfo.isDurable()) {
                String subId = subInfo.getSubscriptionId();
                Topic topic = (Topic) destination;
                if (StringUtils.isEmpty(selector)) {
                    consumer = session.createDurableSubscriber(topic, subId);
                } else {
                    consumer = session.createDurableSubscriber(topic, subId, selector, true);
                }

            } else {
                if (StringUtils.isEmpty(selector)) {
                    consumer = session.createConsumer(destination);
                } else {
                    consumer = session.createConsumer(destination, selector);
                }
            }
        } else {
            if (StringUtils.isEmpty(selector)) {
                consumer = session.createConsumer(destination);
            } else {
                consumer = session.createConsumer(destination, selector);
            }
        }
        return consumer;
    }

    public static MessageProducer createMessageProducer(Context context, Session session,
            PubInfo pubInfo) throws Exception {
        Object dest = context.lookup(pubInfo.getDestinationName());
        validateDestination(dest, pubInfo);
        Destination destination = (Destination) dest;
        MessageProducer producer = session.createProducer(destination);
        if (!pubInfo.isPersistent())
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        return producer;
    }

    protected static void validateDestination(Object dest, BaseInfo info) throws Exception {
        if (info.isTopic()) {
            if (!(dest instanceof Topic)) {
                throw new Exception("Invalid topic object");
            }
        } else {
            if (!(dest instanceof Queue)) {
                throw new Exception("Invalid queue object");
            }
        }
    }

    protected static void validateConnectionFactory(Object cf, BaseInfo info) throws Exception {
        if (info.isTopic()) {
            if (!(cf instanceof TopicConnectionFactory)) {
                throw new Exception("Invalid topic connection factory object");
            }
        } else {
            if (!(cf instanceof QueueConnectionFactory)) {
                throw new Exception("Invalid queue connection factory object");
            }
        }
    }

    public static void closeMessageConsumer(MessageConsumer mc) {
        if (mc != null) {
            try {
                mc.close();
            } catch (JMSException e) {
                //ignore
            }
        }
    }

    public static void closeMessageProducer(MessageProducer mp) {
        if (mp != null) {
            try {
                mp.close();
            } catch (JMSException e) {
                //ignore
            }
        }
    }

    public static void closeSession(Session session) {
        if (session != null) {
            try {
                session.close();
            } catch (JMSException e) {
                //ignore
            }
        }
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (JMSException e) {
                //ignore
            }
        }
    }

    public static void closeContext(Context context) {
        if (context != null) {
            try {
                context.close();
            } catch (Exception e) {
                //ignore
            }
        }
    }
}
