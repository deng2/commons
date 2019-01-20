package com.greenbee.tutorials.hibernate.test;

import java.io.Serializable;
import java.util.Date;

import org.hibernate.Session;

import com.greenbee.commons.hibernate.SessionOperation;
import com.greenbee.tutorials.hibernate.HibernateUtil;
import com.greenbee.tutorials.hibernate.model.mappig.Role;
import com.greenbee.tutorials.hibernate.model.mappig.Child;
import com.greenbee.tutorials.hibernate.model.mappig.Event;
import com.greenbee.tutorials.hibernate.model.mappig.Parent;
import com.greenbee.tutorials.hibernate.model.mappig.Person;
import com.greenbee.tutorials.hibernate.model.mappig.User;

import junit.framework.TestCase;

public class MappingMain extends TestCase {

    public void testManyToMany() {
        final Long id = (Long) HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(Session session) throws Exception {
                Event event = new Event();
                event.setTitle("Birday");
                event.setUpdateTime(new Date());
                Person person = new Person();
                person.setAge(10);
                person.setName("ydeng");
                person.getEvents().add(event);
                return session.save(person);
            }
        });

        HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(Session session) throws Exception {
                Person person = (Person) session.get(Person.class, id);
                log(person);
                return null;
            }
        });
    }

    public void testManyToMany2() {
        final Long userId = (Long) HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(Session session) throws Exception {
                User user = new User();
                user.setName("user1");
                Serializable id = session.save(user);
                return id;
            }
        });
        final Long roleId = (Long) HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(Session session) throws Exception {
                Role role = new Role();
                role.setName("role1");
                return session.save(role);
            }
        });

        final Long roleId2 = (Long) HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(Session session) throws Exception {
                Role role = new Role();
                role.setName("role2");
                return session.save(role);
            }
        });

        HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(Session session) throws Exception {
                User user = (User) session.get(User.class, userId);
                Role role1 = (Role) session.get(Role.class, roleId);
                Role role2 = (Role) session.get(Role.class, roleId2);
                user.getRoles().add(role1);
                user.getRoles().add(role2);
                return null;
            }
        });

        HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(Session session) throws Exception {
                Role role1 = (Role) session.get(Role.class, roleId);
                session.delete(role1);
                return null;
            }
        });

        HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(Session session) throws Exception {
                User user = (User) session.get(User.class, userId);
                session.delete(user);
                return null;
            }
        });

        HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(Session session) throws Exception {
                Role role2 = (Role) session.get(Role.class, roleId2);
                session.delete(role2);
                return null;
            }
        });
    }

    public void testOneToManyBidirectional() {
        //insert
        final Long id = (Long) HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(Session session) throws Exception {
                Parent parent = new Parent();
                parent.setName("ydeng");
                Child child = new Child();
                child.setName("cxdeng");
                parent.getChildren().add(child);
                Serializable pid = session.save(parent);
                return pid;
            }
        });

        //load
        HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(Session session) throws Exception {
                Parent parent = (Parent) session.load(Parent.class, id);
                log(parent);
                return null;
            }
        });

        //add one more child
        HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(Session session) throws Exception {
                Parent parent = (Parent) session.load(Parent.class, id);
                Child child = new Child();
                child.setName("yxdeng");
                parent.getChildren().add(child);
                return null;
            }
        });

        //load
        HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(Session session) throws Exception {
                Parent parent = (Parent) session.load(Parent.class, id);
                log(parent);
                return null;
            }
        });

        //remove all
        HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(Session session) throws Exception {
                Parent parent = (Parent) session.load(Parent.class, id);
                Child child = new Child();
                child.setName("yxdeng");
                parent.getChildren().clear();
                return null;
            }
        });
        //load
        HibernateUtil.execute(new SessionOperation() {
            @Override
            public Object operate(Session session) throws Exception {
                Parent parent = (Parent) session.load(Parent.class, id);
                log(parent);
                return null;
            }
        });
    }

    protected static void log(Object msg) {
        System.out.println(msg);
    }
}
