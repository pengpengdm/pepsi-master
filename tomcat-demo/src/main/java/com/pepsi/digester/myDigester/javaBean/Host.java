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
public class Host {

    private String name;
    private String appBase;
    private String unpackWARs;
    private String autoDeploy;
    private List<Valve> valves = new ArrayList<>();

    public void addChild(Context o){
        System.out.println("Host addChild");
    }

    public void  addValve(Valve valve){
        valves.add(valve);
    }
}
