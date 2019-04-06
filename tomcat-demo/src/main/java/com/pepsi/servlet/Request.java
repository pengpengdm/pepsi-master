package com.pepsi.servlet;

import javax.servlet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/12/21
 * describe:
 */
public class Request implements ServletRequest{
    private InputStream inputStream;
    private String uri;


    public String getUri() {
        return uri;
    }

    public Request(InputStream inputStream){
        this.inputStream = inputStream;
    }

    public void parse() {
        byte[] bytes = new byte[1024];
        StringBuffer sb = new StringBuffer();
        try {
            int i = inputStream.read(bytes);
            for (int j = 0; j < i; j++) {
                sb.append((char)bytes[j]);
            }
            System.out.println(sb.toString());
            uri = parseUri(sb.toString());
        }catch (Exception e){

        }
    }

    private String parseUri(String request) {
        int index1,index2;
        index1 = request.indexOf(' ');
        if(index1 != -1){
            index2 = request.indexOf(' ',index1+1);
            if(index2>index1){
                return request.substring(index1+1,index2);
            }
        }
        return null;
    }


    public Object getAttribute(String s) {
        return null;
    }

    
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    
    public String getCharacterEncoding() {
        return null;
    }

    
    public void setCharacterEncoding(String s) throws UnsupportedEncodingException {

    }

    
    public int getContentLength() {
        return 0;
    }

    
    public String getContentType() {
        return null;
    }

    
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }

    
    public String getParameter(String s) {
        return null;
    }

    
    public Enumeration<String> getParameterNames() {
        return null;
    }

    
    public String[] getParameterValues(String s) {
        return new String[0];
    }

    
    public Map<String, String[]> getParameterMap() {
        return null;
    }

    
    public String getProtocol() {
        return null;
    }

    
    public String getScheme() {
        return null;
    }

    
    public String getServerName() {
        return null;
    }

    
    public int getServerPort() {
        return 0;
    }

    
    public BufferedReader getReader() throws IOException {
        return null;
    }

    
    public String getRemoteAddr() {
        return null;
    }

    
    public String getRemoteHost() {
        return null;
    }

    
    public void setAttribute(String s, Object o) {

    }

    
    public void removeAttribute(String s) {

    }

    
    public Locale getLocale() {
        return null;
    }

    
    public Enumeration<Locale> getLocales() {
        return null;
    }

    
    public boolean isSecure() {
        return false;
    }

    
    public RequestDispatcher getRequestDispatcher(String s) {
        return null;
    }

    
    public String getRealPath(String s) {
        return null;
    }

    
    public int getRemotePort() {
        return 0;
    }

    
    public String getLocalName() {
        return null;
    }

    
    public String getLocalAddr() {
        return null;
    }

    
    public int getLocalPort() {
        return 0;
    }

    
    public ServletContext getServletContext() {
        return null;
    }

    
    public AsyncContext startAsync() {
        return null;
    }

    
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) {
        return null;
    }

    
    public boolean isAsyncStarted() {
        return false;
    }

    
    public boolean isAsyncSupported() {
        return false;
    }

    
    public AsyncContext getAsyncContext() {
        return null;
    }

    
    public DispatcherType getDispatcherType() {
        return null;
    }
}
