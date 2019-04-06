package com.pepsi.connector.processor;

import com.pepsi.connector.PrimitiveServlet;
import com.pepsi.connector.http.HttpRequest;
import com.pepsi.connector.http.HttpRequestFacade;
import com.pepsi.connector.http.HttpResponse;
import com.pepsi.connector.http.HttpResponseFacade;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/12/22
 * describe:
 */
public class ServletProcessor extends Processor{


    @Override
    boolean match(String url) {
        return  url != null && url.startsWith("/servlet");
    }

    @Override
    protected void action(HttpRequest request, HttpResponse response) {
        try {
            Class clazz =  Class.forName("com.pepsi.connector.PrimitiveServlet");
            PrimitiveServlet servlet = (PrimitiveServlet) clazz.newInstance();

            HttpResponseFacade responseFacade = new HttpResponseFacade(response);
            HttpRequestFacade requestFacade = new HttpRequestFacade(request);
            servlet.service(requestFacade, responseFacade);
            ((HttpResponse) response).finishResponse();
        } catch (Exception e) {
            System.out.println(""+e.getMessage());
        } catch (Throwable e) {
            System.out.println(""+ e.getMessage());
        }
    }
}
