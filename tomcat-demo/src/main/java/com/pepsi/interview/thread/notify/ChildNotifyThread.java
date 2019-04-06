package com.pepsi.interview.thread.notify;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/21
 * describe:
 */
public class ChildNotifyThread implements Runnable{

    private Object lock;
    String threadName;

    ChildNotifyThread(Object lock,String threadName){
        this.lock = lock;
        this.threadName = threadName;
    }
    @Override
    public void run() {
        synchronized (lock){
            try {
                System.out.println("threadName="+threadName+",is wait()");
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("threadName="+threadName+",is started");
        }
    }


}
