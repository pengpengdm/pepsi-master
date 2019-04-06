package com.pepsi.coyote.utils;

import java.io.File;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/12/25
 * describe:
 */
public class Constants {
    public static final String WEB_ROOT = String.format("%s%swebroot", System.getProperty("user.dir"), File.separator);
    public static final String PACKAGE = "me.w1992wishes.tomcatwork.simple_tomcat_04.connector.http";
    public static final int DEFAULT_CONNECTION_TIMEOUT = 60000;
    public static final int PROCESSOR_IDLE = 0;
    public static final int PROCESSOR_ACTIVE = 1;

}
