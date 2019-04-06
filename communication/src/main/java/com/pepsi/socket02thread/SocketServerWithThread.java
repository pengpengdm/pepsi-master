package com.pepsi.socket02thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/10/03
 * describe:
 */
public class SocketServerWithThread {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8300);
        InputStream in ;
        OutputStream out ;
        while(true){
            Socket socket = server.accept();
            if(socket!=null) {
                in = socket.getInputStream();
                out = socket.getOutputStream();
                int max = 1024;
                byte[] bytes = new byte[max];
                in.read(bytes, 0, max);
                String msg = new String(bytes, "UTF-8");
                System.out.println("服务器收到来自于端口的信息： + message=" + msg);
                out.write("服务端返回消息".getBytes());
                socket.close();
                in.close();
                out.close();
            }
        }
    }



}
