package com.pepsi.connector.processor;

import com.pepsi.connector.http.HttpRequest;
import com.pepsi.connector.http.HttpResponse;

import java.io.IOException;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/12/24
 * describe:
 */
public class StaticResourceProcessor extends Processor {


    @Override
    boolean match(String url) {
        return url != null && url.startsWith("/resource");
    }

    @Override
    protected void action(HttpRequest request, HttpResponse response) {
        try {
            response.sendStaticResource();
        } catch (IOException e) {
            System.out.println("send static resource failure"+ e.getMessage());
        }
    }
}
