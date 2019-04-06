package com.pepsi.digester.myDigester.digester;

import com.pepsi.digester.myDigester.javaBean.Connector;
import com.pepsi.digester.myDigester.javaBean.Server;
import com.pepsi.digester.myDigester.javaBean.Service;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/13
 * describe:
 */
public class Catalina {

    private Server server;

    public void setServer(Server server) {
        System.out.println("Catalina setServer");
        this.server = server;
    }

    public void parse() throws IOException, SAXException {

        Digester digester = new Digester();
        //把当前对象置入栈顶
        digester.push(this);
        //开始设置 Server节点
        digester.addObjectCreate("Server",Server.class);
        digester.addSetProperties("Server");
        digester.addSetNext("Server","setServer");

        //同理 读取Service节点，
        digester.addObjectCreate("Server/Service", Service.class);
        digester.addSetProperties("Server/Service");
        //这个时候栈顶的对象是Server～ 调用setService方法 将Service加入Server对象
        digester.addSetNext("Server/Service", "setService");

        // 读取Connector
        digester.addObjectCreate("Server/Service/Connector", Connector.class);
        digester.addSetProperties("Server/Service/Connector");
        digester.addSetNext("Server/Service/Connector", "addConnector");

        //读取engine host context
        digester.addRuleSet(new EngineRuleSet("Server/Service/"));
        digester.addRuleSet(new HostRuleSet("Server/Service/Engine/"));
        digester.addRuleSet(new ContextRuleSet("Server/Service/Engine/Host/"));

        File file = new File("/Users/pengjian/work/server.xml");

        digester.parse(file);
    }
}
