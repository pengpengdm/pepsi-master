package com.pepsi.digester.myDigester.digester;

import org.xml.sax.SAXException;

import java.io.IOException;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/13
 * describe:
 */
public class DigesterLaunch {

    public static void main(String[] args) throws IOException, SAXException {
        Catalina catalina = new Catalina();
        catalina.parse();
    }


}
