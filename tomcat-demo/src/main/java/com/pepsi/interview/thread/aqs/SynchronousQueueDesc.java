package com.pepsi.interview.thread.aqs;

import java.util.concurrent.SynchronousQueue;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/22
 * describe:
 */
public class SynchronousQueueDesc {

    public static void main(String[] args) throws InterruptedException {
        SynchronousQueue<String> synQueue = new SynchronousQueue<>();
        //报错，Queue full
        synQueue.add("kekou");
        //当前线程同步等待，直到有其他操作线程取走这个对象
        synQueue.put("pepsi");
    }
}
