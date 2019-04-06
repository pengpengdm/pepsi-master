package com.pepsi.socket02thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/10/04
 * describe:
 */
public class SocketClient02 implements Runnable{

    private CountDownLatch latch;
    private int index;

    public SocketClient02(CountDownLatch latch,int index){
        this.index = index;
        this.latch = latch;
    }

    @Override
    public void run() {
        Socket socket;
        InputStream in = null;
        OutputStream out = null;
        try{
            socket = new Socket("localhost",8300);
            out = socket.getOutputStream();
            in = socket.getInputStream();
            //模拟并发
            this.latch.await();
            //send
            out.write(("this is"+index+"client request").getBytes());
            out.flush();
            System.out.println("第" + this.index + "个客户端的请求发送完成，等待服务器返回信息");
            int maxLenth = 1024;
            byte[] bytes = new byte[maxLenth];
            String message = "";
            int realLen;
            //程序执行到这里，会一直等待服务器返回信息（注意，前提是in和out都不能close，如果close了就收不到服务器的反馈了）
            while((realLen = in.read(bytes, 0, maxLenth)) != -1) {
                message += new String(bytes , 0 , realLen);
            }
            System.out.println("接收到来自服务器的信息:" + message);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                if(in != null) {
                    in.close();
                }
                if(out != null) {
                    out.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }



    }
}
