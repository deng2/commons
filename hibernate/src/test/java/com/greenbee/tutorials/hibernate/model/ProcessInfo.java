package com.greenbee.tutorials.hibernate.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "vt_processinfo")
public class ProcessInfo {

    // State constants.
    public static final int CS_COMPLETE = (int) (1024);
    public static final int CS_TERMINATED = (int) (2048);
    public static final int CS_ACTIVE = (int) (4096);

    private ComponentKey componentKey_;

    private int versionNum_;
    private String bpState_;
    private int bpControlState_ = CS_ACTIVE;
    private int numTransitions_;
    private Date timeEntered_;
    private Date timeCreated_;

    private byte[] state_;

    public ProcessInfo() {
    }

    @EmbeddedId
    public ComponentKey getComponentKey() {
        return componentKey_;
    }

    public void setComponentKey(ComponentKey componentKey) {
        componentKey_ = componentKey;
    }

    @Column(name = "versionNum")
    public int getVersionNum() {
        return versionNum_;
    }

    public void setVersionNum(int versionNum) {
        versionNum_ = versionNum;
    }

    @Column(name = "bpstate")
    public String getBpState() {
        return bpState_;
    }

    public void setBpState(String bpState) {
        bpState_ = bpState;
    }

    @Column(name = "bpcontrolstate")
    public int getBpControlState() {
        return bpControlState_;
    }

    public void setBpControlState(int bpControlState) {
        bpControlState_ = bpControlState;
    }

    @Column(name = "numtransitions")
    public int getNumTransitions() {
        return numTransitions_;
    }

    public void setNumTransitions(int numTransitions) {
        numTransitions_ = numTransitions;
    }

    @Column(name = "timeentered")
    public Date getTimeEntered() {
        return timeEntered_;
    }

    public void setTimeEntered(Date timeEntered) {
        timeEntered_ = timeEntered;
    }

    @Column(name = "timecreated")
    public Date getTimeCreated() {
        return timeCreated_;
    }

    public void setTimeCreated(Date timeCreated) {
        timeCreated_ = timeCreated;
    }

    @Column(name = "bpvariableset")
    public byte[] getBpVariableSet() throws Exception {
        return state_;
    }

    public void setBpVariableSet(byte[] state) throws Exception {
        state_ = state;
    }

}
