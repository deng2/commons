package com.greenbee.tutorials.hibernate.model.mappig;

import java.util.HashSet;
import java.util.Set;

public class Parent {
    private long id;
    private String name;

    private Set<Child> children = new HashSet<Child>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Child> getChildren() {
        return children;
    }

    public void setChildren(Set<Child> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return String.format("Parent\n\tid=%s\n\tname=%s\n\tchildren=%s)", id, name, children);
    }

}
