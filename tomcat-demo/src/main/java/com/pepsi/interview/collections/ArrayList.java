package com.pepsi.interview.collections;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/21
 * describe: 学习类
 */
public class ArrayList implements Cloneable {

    public static void main(String[] args) throws CloneNotSupportedException {
        ArrayList arrayList = new ArrayList();
        Object object = arrayList.clone();
        System.out.println(arrayList.toString());
        System.out.println(object.toString());
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    

}
