package com.pepsi.socket;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/12/20
 * describe:
 */
public class HttpServer {
    public static final  String WEB_ROOT=System.getProperty("user.dir")+ File.separator+"webroot";
    private static final String  SHUTDOWN_COMMAND="/SHUTDOWN";
    private boolean shutdown = false;


    public static void main(String[] args) {
        HttpServer server = new HttpServer();
        server.await();
    }


    private void await() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8080,1, InetAddress.getByName("localhost"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        while(!shutdown){
            InputStream inputStream ;
            OutputStream outputStream;
            Socket socket;
            try {
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                Request request = new Request(inputStream);
                request.parse();
                Response response = new Response(outputStream);
                response.setRequest(request);
                response.sendStaticResource();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

}
