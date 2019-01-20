package com.greenbee.tutorials.hibernate.model.mappig;

import java.util.HashSet;
import java.util.Set;

public class User {
    private long id;
    private String name;

    private Set<Role> roles = new HashSet<>();

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

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", roles=" + roles + "]";
    }

}
