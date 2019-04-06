package com.pepsi.digester.myDigester.javaBean;

import lombok.Data;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/13
 * describe:
 */
@Data
public class Context {
    private String path;
    private String docBase;
    private String reloadable;

    public void addChild(){
        System.out.println("Context addChild");
    }

}
