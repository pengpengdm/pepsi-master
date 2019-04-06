package com.pepsi.digester.myDigester.javaBean;

import lombok.Data;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/13
 * describe:
 */
@Data
public class Connector {

    private String port;

    private String protocol;

    private String connectionTimeout;

    private String redirectPort;


}
