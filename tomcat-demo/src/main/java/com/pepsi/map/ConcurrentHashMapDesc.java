package com.pepsi.map;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/23
 * describe:
 */
public class ConcurrentHashMapDesc {

    public static void main(String[] args) {

        ConcurrentHashMap<String,String> map = new ConcurrentHashMap<>();

        map.putIfAbsent("key","pepsi");
        map.putIfAbsent("name","pengjian");
        map.putIfAbsent("version","1.0.0");
        map.putIfAbsent("app","onstar");

        System.out.println("pepsi");
    }

}
