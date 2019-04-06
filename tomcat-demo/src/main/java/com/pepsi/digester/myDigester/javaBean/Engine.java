package com.pepsi.digester.myDigester.javaBean;

import lombok.Data;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/13
 * describe:
 */
@Data
public class Engine {

    private String name ;

    private String defaultHost ;

    public void addChild(Host o){
        System.out.println("Engine addChild");
    }

}

