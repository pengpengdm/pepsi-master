package com.pepsi.coyote;

import org.apache.catalina.LifecycleException;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/12/24
 * describe:
 */
public interface Lifecycle {


    /**
     * The LifecycleEvent type for the "component start" event.
     */
     String START_EVENT = "start";


    /**
     * The LifecycleEvent type for the "component before start" event.
     */
     String BEFORE_START_EVENT = "before_start";


    /**
     * The LifecycleEvent type for the "component after start" event.
     */
     String AFTER_START_EVENT = "after_start";


    /**
     * The LifecycleEvent type for the "component stop" event.
     */
     String STOP_EVENT = "stop";


    /**
     * The LifecycleEvent type for the "component before stop" event.
     */
     String BEFORE_STOP_EVENT = "before_stop";


    /**
     * The LifecycleEvent type for the "component after stop" event.
     */
     String AFTER_STOP_EVENT = "after_stop";

    void start() throws LifecycleException;

    void stop() throws LifecycleException;
}
