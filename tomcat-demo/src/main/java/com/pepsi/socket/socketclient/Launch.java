package com.pepsi.socket.socketclient;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/12/24
 * describe:
 */
public class Launch {

    public static final String IP_ADDR = "127.0.0.1";//服务器地址
    public static final int PORT = 8080;//服务器端口号

    public static void main(String[] args) {
        try {
            //创建Socket对象
            Socket socket=new Socket(IP_ADDR,PORT);

            //根据输入输出流和服务端连接
            OutputStream outputStream=socket.getOutputStream();//获取一个输出流，向服务端发送信息
            PrintWriter printWriter=new PrintWriter(outputStream);//将输出流包装成打印流
            printWriter.print("GET /servlet/PrimitiveServlet HTTP/1.1\n" +
                    "Host: localhost:8080\n" +
                    "Connection: keep-alive\n" +
                    "Cache-Control: max-age=0\n" +
                    "Upgrade-Insecure-Requests: 1\n" +
                    "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36\n" +
                    "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8\n" +
                    "Accept-Encoding: gzip, deflate, br\n" +
                    "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8\n" +
                    "Cookie: Idea-368c88e1=0ab9bad3-7c85-4ff3-8eb9-43b05fdcc241; onstarcar_username=pepsi; onstarcar_token=dd54ae011558e6fd; JSESSIONID=1FABECE8924A8D29B064221474BF165E\n" +
                    "\n");
            printWriter.flush();
            socket.shutdownOutput();//关闭输出流

            InputStream inputStream=socket.getInputStream();//获取一个输入流，接收服务端的信息
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);//包装成字符流，提高效率
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);//缓冲区
            String info="";
            String temp=null;//临时变量
            while((temp=bufferedReader.readLine())!=null){
                info+=temp;
                System.out.println("客户端接收服务端发送信息："+info);
            }

            //关闭相对应的资源
            bufferedReader.close();
            inputStream.close();
            printWriter.close();
            outputStream.close();
            socket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
