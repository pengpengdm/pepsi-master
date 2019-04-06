package com.pepsi.interview.thread;

import java.io.IOException;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/21
 * describe:
 */
public class ThreadMonitorStateDemo {

    private static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException, IOException {
        Thread threadA = new Thread(() -> {
            //lock锁状态检查，分配给当前的线程分配一把钥匙，并独占这个对象()
            synchronized (lock){
                System.out.println("i am pepsi!");
            }
        });

        Thread threadB = new Thread(() -> {
            //锁状态判断，发现lock锁芯被占(A线程)，等待A释放锁芯
            synchronized (lock){
                System.out.println("i am kekou!");
            }
        });
        threadB.start();
        threadA.start();
        System.in.read();
    }


}
