package com.pepsi.digester.myDigester.javaBean;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/13
 * describe:
 */

public class Server {

    private String port;
    private String shutdown;
    private Service service;

    public void setService(Service service){
        System.out.println("Server setService");
    }
}
