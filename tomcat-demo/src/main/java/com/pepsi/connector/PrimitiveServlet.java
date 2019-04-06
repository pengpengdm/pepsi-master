package com.pepsi.connector;

import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

public class PrimitiveServlet implements Servlet{
    
    public void init(ServletConfig config) throws ServletException{
        System.out.println("init");
    }
    public void service(ServletRequest request, ServletResponse response) throws IOException {

        System.out.println("servlet {} start,"+ getClass().getSimpleName());

        PrintWriter out = response.getWriter();
        out.println("hello, roses are red");
        out.println("------------------------");
        out.println("violets are blue");

        System.out.println("servlet {} start,"+getClass().getSimpleName());

    }
    public void destroy(){
        System.out.println("destroy");
    }
    public String getServletInfo(){
        return null;
    }
    public ServletConfig getServletConfig(){
        return null;
    }
}