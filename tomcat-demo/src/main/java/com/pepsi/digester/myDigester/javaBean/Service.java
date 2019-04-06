package com.pepsi.digester.myDigester.javaBean;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/13
 * describe:
 */
@Data
public class Service {

    private String name;

    private List<Connector> connectorList = new ArrayList<>();

    private Engine engine;

    public void  addConnector(Connector connector){
        System.out.println("Service addConnector");
        connectorList.add(connector);
    }

}
