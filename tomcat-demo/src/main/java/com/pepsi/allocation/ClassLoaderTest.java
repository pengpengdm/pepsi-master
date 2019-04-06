package com.pepsi.allocation;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/20
 * describe:
 */
public class ClassLoaderTest {
    public static void main(String[] args) {
        //当前类加载器
        ClassLoader cl = SimpleClass.class.getClassLoader();
        System.out.println(cl);
        //父类加载器
        ClassLoader parentCLoader= cl.getParent();
        System.out.println(parentCLoader);
        //父类的父类
        ClassLoader grandParentCl= parentCLoader.getParent();
        System.out.println(grandParentCl);
    }
}
