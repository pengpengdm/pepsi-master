package com.pepsi.connector.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/12/22
 * describe: 连接器
 */
public class HttpConnector implements Runnable{
    private boolean stop = false;
    @Override
    public void run() {
        ServerSocket serverSocket;
        try{
            serverSocket  = new ServerSocket(8080,1, InetAddress.getByName("127.0.0.1"));
            while(!stop){
                Socket socket = serverSocket.accept();
                HttpProcessor processor = new HttpProcessor();
                processor.process(socket);
            }

        }catch (Exception e){

        }
    }
}
