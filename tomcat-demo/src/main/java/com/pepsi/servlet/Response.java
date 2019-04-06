package com.pepsi.servlet;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/12/21
 * describe:
 */
public class Response implements ServletResponse{

    private Request request;

    private OutputStream outputStream;

    private PrintWriter writer;

    public Response(OutputStream outputStream){
        this.outputStream = outputStream;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void sendStaticResource(){
        String responseStr = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: 23\r\n" +
                "\r\n" +
                "<h1>Pepsi is found</h1>";
        try {
            outputStream.write(responseStr.getBytes());
        }catch (Exception e){
            String error = "HTTP/1.1 404 File Not Found\r\n" +
                    "Content-type: text/html\r\n" +
                    "Content-length: 23\r\n" +
                    "\r\n" +
                    "<h1>File not found</h1>";
            try {
                outputStream.write(error.getBytes());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }


    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }


    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return null;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if(writer==null){
            writer = new PrintWriter(outputStream,true);
        }
        return writer;
    }


    @Override
    public void setContentLength(int i) {

    }

    @Override
    public void setContentType(String s) {

    }

    @Override
    public void setBufferSize(int i) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() throws IOException {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }
}
