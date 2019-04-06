package com.pepsi.interview.thread.ConsumerProducer;

import java.util.Stack;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/21
 * describe:
 */
public class Storage<T> {
    private int index = 0;
    private static final int MAX = 10;//最大容量
    private Stack<T> stack = new Stack<>();
    public Storage(){ }

    public synchronized void procuder(T t){
        while (index >= MAX) {
            try {
                System.out.println("仓库满了，等待中...");
                wait();
                System.out.println("仓库不满了，开始生产");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("生产>>" + t.toString());
        stack.push(t);
        index++;
        notify();
    }

    public synchronized T consumer() {
        while (index <= 0) {// 判断仓库空了，则等待。
            try {
                System.out.println("仓库为空，等待中...");
                wait();
                System.out.println("仓库不为空，开始消费");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        index--;//先进行减1操作，再remove
        T item = stack.peek();
        System.out.println("消费>>" + item.toString());
        notify();
        return item;
    }
}
