package com.pepsi.connector.processor;

import com.pepsi.connector.http.HttpRequest;
import com.pepsi.connector.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/12/22
 * describe:
 */
public abstract class Processor {

    protected Logger LOGGER = LoggerFactory.getLogger(getClass());

    private Processor processor;

    public void setProcessor(Processor processor) {
        this.processor = processor;
    }

    /**
     * 根据url判断由哪个具体的Processor去处理
     *
     * @param url
     */
    abstract boolean match(String url);

    /**
     * 责任链模式以合适的processor处理
     *
     * @param request
     * @param response
     */
    public void process(HttpRequest request, HttpResponse response){
        if (match(request.getRequestURI())){
            action(request, response);
        } else {
            processor.process(request, response);
        }
    }

    /**
     * 模板方法模式处理
     *
     * @param request
     * @param response
     */
    protected abstract void action(HttpRequest request, HttpResponse response);


}
