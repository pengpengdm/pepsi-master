package com.pepsi.interview.thread.notify;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/21
 * describe:
 */
public class ParentNotifyThread implements Runnable {

    private final Object WAIT_CHILEOBJECT = new Object();

    public static void main(String[] args) {
        new Thread(new ParentNotifyThread()).start();
    }

    @Override
    public void run() {
        for(int index = 0 ; index < 3 ; index++) {
            ChildNotifyThread childNotify = new ChildNotifyThread(WAIT_CHILEOBJECT,"thread-"+index+"pepsi");
            Thread childNotifyThread = new Thread(childNotify);
            childNotifyThread.start();
        }

        synchronized (WAIT_CHILEOBJECT) {
            WAIT_CHILEOBJECT.notifyAll();
        }

        //挡住ParentNotifyThread 不退出
        synchronized (ParentNotifyThread.class){
            try {
                ParentNotifyThread.class.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
