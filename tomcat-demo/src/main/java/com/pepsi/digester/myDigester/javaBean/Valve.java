package com.pepsi.digester.myDigester.javaBean;

import lombok.Data;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/13
 * describe:
 */
@Data
public class Valve {
    private String className;
    private String directory;
    private String prefix;
    private String suffix;
    private String pattern;
}
