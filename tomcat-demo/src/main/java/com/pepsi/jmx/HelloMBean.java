package com.pepsi.jmx;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/07
 * describe:
 */
public interface HelloMBean {

    String getName();
    void setName(String name);
    void printHello();
    void printHello(String whoName);

}
