package com.pepsi.interview.thread.aqs;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/22
 * describe:
 */
public class ReentrantLockDesc {

    public static void main(String[] args) {

        final ReentrantLock objectLock = new ReentrantLock();

        new Thread(() -> {
            objectLock.lock();
            System.out.println("做了一些事情。。。。");
            objectLock.unlock();
        }).start();

        new Thread(() -> {
            objectLock.lock();
            System.out.println("做了另一些事情。。。。");
            objectLock.unlock();
        }).start();

    }
}
