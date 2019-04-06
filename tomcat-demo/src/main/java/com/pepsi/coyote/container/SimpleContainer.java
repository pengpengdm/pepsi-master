package com.pepsi.coyote.container;



import com.pepsi.coyote.http.HttpRequest;
import com.pepsi.coyote.http.HttpResponse;
import com.pepsi.coyote.processor.DefaultProcessor;
import com.pepsi.coyote.processor.Processor;
import com.pepsi.coyote.processor.ServletProcessor;
import com.pepsi.coyote.processor.StaticResourceProcessor;

import javax.servlet.ServletException;
import java.io.File;
import java.io.IOException;


public class SimpleContainer implements Container{

    public static final String WEB_ROOT = System.getProperty("user.dir") + File.separator + "webroot";

    public SimpleContainer(){}

    @Override
    public void invoke(HttpRequest request, HttpResponse response) throws IOException, ServletException {
        //check if this is a request for a servlet or a static resource
        //a request for a servlet begins with "/servlet/"
        Processor servletProcessor = new ServletProcessor();
        Processor staticProcessor = new StaticResourceProcessor();
        Processor defaultProcessor = new DefaultProcessor();
        staticProcessor.setProcessor(defaultProcessor);
        servletProcessor.setProcessor(staticProcessor);
        servletProcessor.process(request, response);
    }
}
