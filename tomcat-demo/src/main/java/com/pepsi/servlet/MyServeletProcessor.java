package com.pepsi.servlet;

import javax.servlet.Servlet;
import java.net.URLStreamHandler;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/12/21
 * describe:
 */
public class MyServeletProcessor {

    public void processor(Request request, Response response) {
        String uri = request.getUri();
        String servletName = uri.substring(uri.lastIndexOf('/')+1);
        ClassLoader classLoader;
        URLStreamHandler handler =null;
        try{
            RequestFacade facade = new RequestFacade(request);
            ResponseFacade responseFacade = new ResponseFacade(response);
            /*URL[] urls = new URL[1];
            File classPath = new File(Constants.WEB_ROOT);
            String repository = (new URL("file",null,classPath.getCanonicalPath()+ File.separator)).toString();
            urls[0] = new URL(null,repository,handler);*/
//            classLoader = MyServeletProcessor.class.getClassLoader();
            Class clazz = Class.forName("com.pepsi.servlet.MyServlet");
//            Class myclass = classLoader.loadClass("MyServlet");
            Servlet servlet = (Servlet) clazz.newInstance();
            servlet.service(facade, responseFacade);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }





    }
}
