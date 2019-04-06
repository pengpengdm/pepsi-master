package com.pepsi.jmx.dynamicMB;

import javax.management.*;
import java.lang.management.ManagementFactory;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/08
 * describe:
 */
public class DynamicAgent {

    public static void main(String[] args) throws MalformedObjectNameException, NotCompliantMBeanException, InstanceAlreadyExistsException, MBeanRegistrationException, InterruptedException {
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("pepsi:name=HelloDynamic");
        HelloDynamic hello = new HelloDynamic();
        server.registerMBean(hello, objectName);
        Thread.sleep(60*60*1000);
    }
}
