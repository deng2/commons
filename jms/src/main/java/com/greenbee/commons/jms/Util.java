// Copyright (c) 2012 by Vitria Technology, Inc.
// All Rights Reserved.
//
package com.greenbee.commons.jms;

import com.greenbee.commons.StringUtils;

public class Util {

    public static PubInfo createPubInfo(String cf, String dest, boolean isTopic,
            boolean persistent) {
        if (StringUtils.isEmpty(cf)) {
            cf = "ConnectionFactory";
        }
        PubInfo pubInfo = new PubInfo();
        pubInfo.setConnectionFactoryName(cf);
        pubInfo.setDestinationName(dest);
        pubInfo.setTopic(isTopic);
        pubInfo.setPersistent(persistent);
        return pubInfo;
    }

    public static SubInfo createSubInfo(String cf, String dest, boolean isTopic, boolean isDurable,
            String clientID, String subscriptionID) {
        if (StringUtils.isEmpty(cf)) {
            cf = "ConnectionFactory";
        }
        SubInfo subInfo = new SubInfo();
        subInfo.setConnectionFactoryName(cf);
        subInfo.setDestinationName(dest);
        subInfo.setTopic(isTopic);
        subInfo.setDurable(isDurable);
        subInfo.setClientId(clientID);
        subInfo.setSubscriptionId(subscriptionID);
        ConnectionLib.enrichSubInfo(subInfo);
        return subInfo;
    }

}
