// Copyright (c) 2014 Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.jms;

public class SubInfo extends BaseInfo {
    private boolean isDurable = false;
    private String subscriptionId;
    private String clientId;
    private String messageSelector;

    public SubInfo() {
        super();

    }

    /**
     *
     * @return true if want to use durable subscription style to subscribe to the topic
     */
    public boolean isDurable() {
        return isDurable;
    }

    public void setDurable(boolean isDurable) {
        this.isDurable = isDurable;
    }

    /**
     *
     * @return subscription name used to setup the durable subscription
     */
    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    /**
     *
     * @return client ID used to setup durable subscription
     */
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getMessageSelector() {
        return messageSelector;
    }

    public void setMessageSelector(String messageSelector) {
        this.messageSelector = messageSelector;
    }
}
