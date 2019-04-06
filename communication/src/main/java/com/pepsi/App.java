package com.pepsi;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) {


        String[] strso = new String[]{"1","2","3"};

        String[] strs = new String[4];
        strs[0] = "1";
        strs[1] = "2";









        System.out.println("strs length="+strs.length);
        //1

        List<String> l = new ArrayList<>();
        l.add("1");
        l.add("2");
        l.add("3");
        l.add("4");
        l.add("5");
        l.add(3,"10");

        System.out.println(l);
        System.out.println( strs );

        int legn = 10;
        System.out.println(legn>>1);
    }
}
