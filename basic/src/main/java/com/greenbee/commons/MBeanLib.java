package com.greenbee.commons;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

public class MBeanLib {
    public static MBeanServer getMBeanServer() {
        return ManagementFactory.getPlatformMBeanServer();
    }
}
