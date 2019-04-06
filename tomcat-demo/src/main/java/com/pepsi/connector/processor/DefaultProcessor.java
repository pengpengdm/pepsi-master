package com.pepsi.connector.processor;

import com.pepsi.connector.http.HttpRequest;
import com.pepsi.connector.http.HttpResponse;

import java.io.IOException;
import java.io.Writer;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/12/22
 * describe:
 */
public class DefaultProcessor extends Processor {
    @Override
    boolean match(String url) {
        return true;
    }

    @Override
    protected void action(HttpRequest request, HttpResponse response) {
        Writer writer = null;
        try {
            writer = response.getWriter();
            String message = "no suitable processor";
            writer.write(message);
        } catch (IOException e) {
           System.out.println("get response writer failure");
        } finally {
            if (writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                   System.out.println("close writer failure");
                }
            }
        }
    }
}
