package com.pepsi;

import java.io.*;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) throws IOException {
        byte[] bytes = new byte[1024];
        InputStream inputStream = new FileInputStream(new File("/Users/pengjian/Downloads/ota.json"));
        int i = inputStream.read(bytes,0,1024);
        String str = new String(bytes,0,i);
        System.out.println(i+",str="+str);
    }
}
