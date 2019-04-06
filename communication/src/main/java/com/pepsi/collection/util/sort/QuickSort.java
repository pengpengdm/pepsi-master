package com.pepsi.collection.util.sort;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/02/28
 * describe: 快速排序
 */
public class QuickSort implements Sort {


    @Override
    public void sort(int[] array) {
        sort(array,0,array.length-1);
    }

    private void sort(int[] array, int left, int right) {
        int index = partition(array,left,right);

        if(left<index-1){
            sort(array,left, index - 1);
        }
        if(index + 1 < right){
            sort(array, index, right);
        }
    }

    private int partition(int[] array, int left, int right) {

        int pivot = array[(left+right)/2];
        int temp;
        while(left<right){

            while(array[left]<pivot){
                left++;
            }
            while(array[right]>pivot){
                right--;
            }

            if(left<right){
                temp = array[left];
                array[left] = array[right];
                array[right] = temp;
                left++;
                right--;
            }
        }
        return left;
    }


    public static void main(String[] args) {
        int[] array = new int[]{10,2,6,8,3,9,7,1,4,5};

        QuickSort quickSort = new QuickSort();

        quickSort.sort(array);

        System.out.println(array);
    }
}
