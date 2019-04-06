package com.pepsi.digester.csdn;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/13
 * describe:
 */
public class Server {
    private String name;
    private String  port;
    private String shutdown;

    private List<Service> serviceList = new ArrayList<>();

    public void addService(Service service){
        System.out.println("Server addService");
        serviceList.add(service);
    }

    public void  play(){
        System.out.println("i like pepsi");
    }
}
