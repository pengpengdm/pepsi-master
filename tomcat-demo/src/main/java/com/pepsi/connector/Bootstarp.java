package com.pepsi.connector;

import com.pepsi.connector.http.HttpConnector;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/12/22
 * describe: 启动类
 */
public class Bootstarp {

    public static void main(String[] args) {
        HttpConnector connector = new HttpConnector();
        Thread thread = new Thread(connector);
        thread.start();
    }
}
