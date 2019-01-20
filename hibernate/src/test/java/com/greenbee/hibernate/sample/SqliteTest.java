package com.greenbee.hibernate.sample;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.BeforeClass;
import org.junit.Test;

import com.greenbee.commons.CommandLineLogger;

public class SqliteTest {
    private static SessionFactory factory;

    @BeforeClass
    public static void setup() {
        factory = new Configuration().configure("sqlite.hibernate.cfg.xml").buildSessionFactory();
    }

    @Test
    public void saveMessage() {
        Message message = new Message();
        message.setText("Hello, world");
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(message);
        CommandLineLogger.print(String.format("id=%s", message.getId()));
        tx.commit();
        session.close();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void readMessage() {
        Session session = factory.openSession();
        List<Message> list = (List<Message>) session.createQuery("from Message").list();
        for (Message m : list) {
            System.out.println(m);
        }
        session.close();
    }

}
