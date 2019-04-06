package com.pepsi.servlet;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/12/21
 * describe:
 */
public class HttpServer {

    private static boolean shutdown = false;


    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        InputStream inputStream ;
        OutputStream outputStream;
        try{
            serverSocket = new ServerSocket(8080,1, InetAddress.getByName("127.0.0.1"));
            while(!shutdown){
                Socket socket = serverSocket.accept();
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                Request request = new Request(inputStream);
                request.parse();

                Response response = new Response(outputStream);
                response.setRequest(request);

                if(request.getUri().startsWith("/servlet")){
                    MyServeletProcessor processor = new MyServeletProcessor();
                    processor.processor(request,response);
                }else{
                    StaticResourceProcessor processor = new StaticResourceProcessor();
                    processor.processor(request,response);
                }
                socket.close();
            }

        }catch (Exception e) {

        }




    }
}
