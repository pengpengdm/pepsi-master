package com.pepsi.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/09/21
 * describe:
 */
public class SocketClient implements Runnable{

    private CountDownLatch countDownLatch;
    private Integer clientIndex;

    public SocketClient(CountDownLatch countDownLatch,Integer clientIndex){
        this.countDownLatch = countDownLatch;
        this.clientIndex = clientIndex;
    }

    @Override
    public void run() {
        Socket socket;
        InputStream clientResponse = null;
        OutputStream clientRequest = null;
        try{
            socket = new Socket("localhost",8300);
            clientRequest = socket.getOutputStream();
            clientResponse = socket.getInputStream();
            //模拟并发
            this.countDownLatch.await();
            //send
            clientRequest.write(("this is"+clientIndex+"client request").getBytes());
            clientRequest.flush();
            System.out.println("第" + this.clientIndex + "个客户端的请求发送完成，等待服务器返回信息");
            int maxLenth = 1024;
            byte[] bytes = new byte[maxLenth];
//            int read = clientResponse.read(bytes,0,maxLenth);
            //String responseMsg = new String(bytes);
            String message = "";
            int realLen;
            //程序执行到这里，会一直等待服务器返回信息（注意，前提是in和out都不能close，如果close了就收不到服务器的反馈了）
            while((realLen = clientResponse.read(bytes, 0, maxLenth)) != -1) {
                message += new String(bytes , 0 , realLen);
            }
            System.out.println("接收到来自服务器的信息:" + message);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }finally {
            try {
                if(clientRequest != null) {
                    clientRequest.close();
                }
                if(clientResponse != null) {
                    clientResponse.close();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
