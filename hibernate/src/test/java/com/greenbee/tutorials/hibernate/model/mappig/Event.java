package com.greenbee.tutorials.hibernate.model.mappig;

import java.util.Date;

public class Event {
    private long id;
    private Date updateTime;

    private String title;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return String.format("Event(id=%s, updateTime=%s, title=%s)", id, updateTime, title);
    }

}
