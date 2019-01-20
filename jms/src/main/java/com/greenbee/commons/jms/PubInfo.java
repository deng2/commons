// Copyright (c) 2014 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.jms;

public class PubInfo extends BaseInfo {
    private boolean persistent = true;

    public PubInfo() {
        super();
    }

    public boolean isPersistent() {
        return persistent;
    }

    public void setPersistent(boolean persistent) {
        this.persistent = persistent;
    }
}
