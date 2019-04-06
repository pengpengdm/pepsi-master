package com.pepsi.digester.csdn;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;
import sun.tools.java.ClassPath;

import java.io.File;
import java.io.IOException;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/13
 * describe:
 */
public class PepsiDigester {
    private Server server;
    public void setServer(Server server) {
        this.server = server;
    }
    public static void main(String[] args) throws IOException, SAXException {
        PepsiDigester digester = new PepsiDigester();
        digester.parseXml();
    }
    public void parseXml() throws IOException, SAXException {
        Digester digester = new Digester();
        //把当前对象置入栈顶
        digester.push(this);
        //开始设置 Server节点
        digester.addObjectCreate("Server", Server.class);
        digester.addSetProperties("Server");
        digester.addCallMethod("Server","play");
        digester.addSetNext("Server","setServer");

        //同理 读取Service节点，
        digester.addObjectCreate("Server/Service", Service.class);
        digester.addSetProperties("Server/Service");
        //这个时候栈顶的对象是Server～ 调用addService方法 将Service加入Server对象
        digester.addSetNext("Server/Service", "addService");

        File file = new File("/Users/pengjian/work/server.xml");

        digester.parse(file);
    }
}
