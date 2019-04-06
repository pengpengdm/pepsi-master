package com.pepsi.servlet;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/12/21
 * describe:
 */
public class StaticResourceProcessor {

    public void processor(Request request, Response response) {
        response.sendStaticResource();
    }
}
