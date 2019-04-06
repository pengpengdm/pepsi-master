package com.pepsi.socket;

import java.io.*;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/12/20
 * describe:
 */
public class Response {

    private static final int BUFF_SIZE = 1024;

    private OutputStream outputStream;

    private Request request;

    public void setRequest(Request request) {
        this.request = request;
    }

    Response(OutputStream outputStream) {
        this.outputStream = outputStream;
    }


    public void sendStaticResource() {
        byte[] bytes = new byte[BUFF_SIZE];
        FileInputStream fileInputStream = null;
        File file = new File(HttpServer.WEB_ROOT,request.getUri());
        try {
            if(file.exists()){
                fileInputStream = new FileInputStream(file);
                int ch = 0;
                String str = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: 22\r\n" +
                        "\r\n" +
                        "<h1>Pepsi is cool</h1>";
               outputStream.write(str.getBytes());
            } else {
                String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
                        "Content-type: text/html\r\n" +
                        "Content-length: 23\r\n" +
                        "\r\n" +
                        "<h1>File not found</h1>";
                outputStream.write(errorMessage.getBytes());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
