package com.greenbee.tutorials.hibernate.model.mappig;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private long id;
    private int age;
    private String name;

    private List<Event> events = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public String toString() {
        return String.format("Person:\n\tid=%s\n\tname=%s\n\tevents=%s", id, name, events);
    }

}
