package com.pepsi.jmx;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/07
 * describe:
 */
public class Hello implements HelloMBean {
    private String name;
    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void printHello() {
        System.out.println("Hello world, "+ name);
    }

    @Override
    public void printHello(String whoName) {
        System.out.println("Hello, "+whoName);
    }
}
