package com.pepsi.digester;

import org.apache.catalina.Server;
import org.apache.tomcat.util.digester.Digester;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/12/19
 * describe: 解析XML
 */
public class MyDigester {

    protected Server server = null;

    public Server getServer() {
        return server;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    protected Digester createStartDigester() {
        // 实例化一个Digester对象
        Digester digester = new Digester();

        // 设置为false表示解析xml时不需要进行DTD的规则校验
        digester.setValidating(false);

        // 是否进行节点设置规则校验,如果xml中相应节点没有设置解析规则会在控制台显示提示信息
        digester.setRulesValidation(true);

        // 将xml节点中的className作为假属性，不必调用默认的setter方法（一般的节点属性在解析时将会以属性值作为入参调用该节点相应对象的setter方法，而className属性的作用是提示解析器用该属性的值来实例化对象）
        HashMap<Class<?>, List<String>> fakeAttributes = new HashMap<>();
        ArrayList<String> attrs = new ArrayList<>();
        attrs.add("className");
        fakeAttributes.put(Object.class, attrs);
        digester.setFakeAttributes(fakeAttributes);

        // addObjectCreate方法的意思是碰到xml文件中的Server节点则创建一个MyStandardServer对象
        digester.addObjectCreate("Server",
                "org.apache.catalina.core.StandardServer", "className");

        // 根据Server节点中的属性信息调用相应属性的setter方法，以上面的xml文件为例则会调用setPort、setShutdown方法，入参分别是8005、SHUTDOWN
        digester.addSetProperties("Server");
        // 将Server节点对应的对象作为入参调用栈顶对象的setMyServer方法，这里的栈顶对象即下面的digester.push方法所设置的当前类的对象this，就是说调用MyDigester类的setMyServer方法
        digester.addSetNext("Server", "setServer",
                "org.apache.catalina.Server");

        // 碰到xml的Server节点下的Listener节点时取className属性的值作为实例化类实例化一个对象
        digester.addObjectCreate("Server/Listener", null, "className");
        digester.addSetProperties("Server/Listener");
        digester.addSetNext("Server/Listener", "addLifecycleListener",
                "org.apache.catalina.LifecycleListener");

        digester.addObjectCreate("Server/Service",
                "org.apache.catalina.core.StandardService", "className");
        digester.addSetProperties("Server/Service");
        digester.addSetNext("Server/Service", "addService",
                "org.apache.catalina.Service");

        digester.addObjectCreate("Server/Service/Listener", null, "className");
        digester.addSetProperties("Server/Service/Listener");
        digester.addSetNext("Server/Service/Listener", "addLifecycleListener",
                "org.apache.catalina.LifecycleListener");
        return digester;
    }

    public MyDigester(){
        Digester digester =  createStartDigester();
        InputSource inputSource = null;
        InputStream inputStream = null;
        try {
            //server.xml 地址
            File file = new File("/Users/pengjian/work/server.xml");
            inputStream = new FileInputStream(file);
            inputSource = new InputSource(file.toURI().toURL().toString());
        } catch (Exception e) {

        }
        try {
            //digester 解析XML
            inputSource.setByteStream(inputStream);
            digester.push(this);
            //返回server信息
            Object object = digester.parse(inputSource);
        } catch (Exception spe) {

        }

    }



}
