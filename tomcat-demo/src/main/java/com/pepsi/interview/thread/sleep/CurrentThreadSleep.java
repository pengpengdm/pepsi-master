package com.pepsi.interview.thread.sleep;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/21
 * describe:
 */
public class CurrentThreadSleep {

    public static void main(String[] args) throws InterruptedException {
        Thread currentThread = Thread.currentThread();
        Thread newThread = new Thread(() -> System.out.println("i am new thread"));
        newThread.start();
        //沉睡的不是newThread，而是一定是当前执行的线程
        newThread.sleep(5000);
        System.out.println("");
    }
}
