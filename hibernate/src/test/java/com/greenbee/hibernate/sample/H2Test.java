package com.greenbee.hibernate.sample;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.BeforeClass;
import org.junit.Test;

import com.greenbee.commons.CommandLineLogger;
import com.greenbee.commons.hibernate.SessionExecutor;

public class H2Test {
    private static SessionFactory factory;
    private static SessionExecutor executor;

    @BeforeClass
    public static void init() {
        factory = new Configuration().configure("h2.hibernate.cfg.xml").buildSessionFactory();
        executor = new SessionExecutor(factory);
    }

    @Test
    public void save() throws Exception {
        executor.execute(session -> {
            Message msg = new Message();
            msg.setText("test");
            session.save(msg);
            CommandLineLogger.log(String.format("id=%s", msg.getId()));
            return null;
        });
    }
}