package com.pepsi.collection.util.sort;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/02/28
 * describe: 冒泡排序--->升序
 * 平均情况与最差情况都是 O(n^2)<br>
 *  空间复杂度O(1)
 */
public class BubblySort implements Sort {

    @Override
    public void sort(int[] array) {
        if(array.length!=0){
            int temp = 0;
            for(int i =0;i<array.length;i++){
                for(int j=0;j<array.length-1-i;j++) {
                    if (array[j] > array[j + 1]) {
                        temp = array[j + 1];
                        array[j + 1] = array[j];
                        array[j] = temp;
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        int[] array = new int[]{10,2,6,8,3,9,7,1,4,5};
        BubblySort bs = new BubblySort();
        bs.sort(array);
        System.out.println("");
    }
}
