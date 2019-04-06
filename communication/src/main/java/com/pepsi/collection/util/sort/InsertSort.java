package com.pepsi.collection.util.sort;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/02/28
 * describe: 插入排序，
 * */
public class InsertSort implements Sort{
    @Override
    public void sort(int[] array) {
        int temp;
        for (int i = 1; i < array.length; i++) {
            temp = array[i];
            int j = i-1;
            while(j>=0 && array[j]>temp){
                array[j+1] = array[j];
                j--;
            }
            if(j!=i-1){
                array[j+1] = temp;
            }
        }
    }


    public static void main(String[] args) {
        int[] array = new int[]{10,2,6,8,3,9,7,1,4,5};
        InsertSort s =new InsertSort();
        s.sort(array);
        System.out.println();
    }
}
