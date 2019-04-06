package com.pepsi.collection.util.sort;

/**
 *
 * @author pepsi
 * @version 1.0
 * @date 2019/02/28
 * describe:选择排序，一个一个往坑里放。
 */
public class SelectSort implements Sort {


    @Override
    public void sort(int[] array) {
        int temp = 0;
        for(int i=0;i<array.length;i++){
            temp = array[i];
            for(int j = i;j<array.length;j++){
                if(array[j]<temp){
                    array[j] = temp;
                }
            }
            if(temp != array[i]){
                array[i] = temp;
            }
        }

    }






}
