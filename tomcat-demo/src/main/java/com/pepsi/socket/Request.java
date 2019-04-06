package com.pepsi.socket;

import java.io.IOException;
import java.io.InputStream; /**
 * @author pepsi
 * @version 1.0
 * @date 2018/12/20
 * describe:
 */
public class Request {

    private InputStream inputStream;
    private String uri;

    public Request(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public void parse() {
        int i = 0;
        StringBuffer stringBuffer = new StringBuffer(1024);
        byte[] bytes = new byte[1024];
        try {
            i = inputStream.read(bytes);
            for (int j = 0; j < i; j++) {
                stringBuffer.append((char)bytes[j]);
            }
            System.out.println(stringBuffer.toString());
            uri = parseUri(stringBuffer.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String parseUri(String requestString) {
        int index1,index2;
        try {
            index1 =requestString.indexOf(' ');
            if(index1!=-1){
                index2 = requestString.indexOf(' ',index1+1);
                if(index2>index1){
                    return requestString.substring(index1+1,index2);
                }
            }
        }catch (Exception e){

        }
        return  null;
    }



    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
