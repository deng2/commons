package com.greenbee.tutorials.hibernate.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ComponentKey implements Serializable {
    private static final long serialVersionUID = 1L;

    private String componentName_;
    private String instanceId_;

    public ComponentKey() {
    }

    public ComponentKey(String componentName, String instanceId) {
        componentName_ = componentName;
        instanceId_ = instanceId;
    }

    @Column(name = "componentName")
    public String getComponentName() {
        return componentName_;
    }

    public void setComponentName(String componentName) {
        componentName_ = componentName;
    }

    @Column(name = "vtoid")
    public String getInstanceId() {
        return instanceId_;
    }

    public void setInstanceId(String instanceId) {
        instanceId_ = instanceId;
    }

    public int hashCode() {
        return (componentName_ + instanceId_).hashCode();
    }

    public boolean equals(Object object) {
        if (this == object)
            return true;
        if (!(object instanceof ComponentKey))
            return false;
        ComponentKey key = (ComponentKey) object;
        return key.componentName_.equals(componentName_) && key.instanceId_.equals(instanceId_);
    }

    public String toString() {
        return "<" + componentName_ + ", " + instanceId_ + ">";
    }

}
