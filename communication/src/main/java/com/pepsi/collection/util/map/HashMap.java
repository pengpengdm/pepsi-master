package com.pepsi.collection.util.map;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/03/01
 * describe:
 */
public class HashMap {


    public static void main(String[] args) {


        String sss = "pepsi";

        Object str = "pepsi";

        int h =0;

        char[] value = new char[5];
        
        
        
        for (int i = 0; i < sss.length(); i++) {
            value[i] = sss.charAt(i);
        }

        if (h == 0 && value.length > 0) {
            char val[] = value;

            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
        }

        System.out.println(h);
        
        System.out.println(h>>>16);

        System.out.println((h=sss.hashCode())^(h >>> 16));

        int d =123;
        for(int i = 31;i >= 0; i--){
            System.out.print(d>>>i & 1);
        }

        System.out.println();

        System.out.println("123的 右移一位是："+(d>>>1));

        int d2 =61;

        //00000000000000000000000001111011
        for(int i = 31;i >= 0; i--){
            System.out.print(d2 >> i & 1);
        }
        System.out.println();

        System.out.println("61的 zuo移一位是："+(d2<<1));


        int d3 =122;

        //00000000000000000000000001111011
        for(int i = 31;i >= 0; i--){
            System.out.print(d3 >> i & 1);
        }


        System.out.println("2d的<<<"+(2<<1));

        System.out.println("2>>>"+(2>>1));

        System.out.println(2^3);



        for(int i = 0;i<255;i++){
            char a = (char) i;
            System.out.println(a+"........."+i);
        }
    }


}
