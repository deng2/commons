package com.greenbee.tutorials.hibernate.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VT_CONNECTORSTATE")

public class ConnectorState {

    private long connectorId_;
    private int instanceId_;
    private String componentFullName_;
    private byte[] state_;

    @Id
    @Column(name = "connectorId")
    public long getConnectorId() {
        return connectorId_;
    }

    public void setConnectorId(long connId) {
        connectorId_ = connId;
    }

    @Column(name = "instanceId")
    public int getInstanceId() {
        return instanceId_;
    }

    public void setInstanceId(int instanceId) {
        instanceId_ = instanceId;
    }

    @Column(name = "componentFullName")
    public String getComponentFullName() {
        return componentFullName_;
    }

    public void setComponentFullName(String componentFullName) {
        componentFullName_ = componentFullName;
    }

    @Column(name = "state")
    public byte[] getState() {
        return state_;
    }

    public void setState(byte[] state) {
        state_ = state;
    }

}
