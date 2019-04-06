package com.pepsi.interview.thread;

import java.util.Stack;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/21
 * describe:
 */
public class Consumer implements Runnable {

    private Object popLock;
    private Stack fruitBasket;
    private Object pushLock;

    public Consumer(Stack fruitBasket,Object popLock,Object pushLock){
        this.fruitBasket = fruitBasket;
        this.popLock = popLock;
        this.pushLock = pushLock;
    }

    @Override
    public void run() {
        while(true){
            try {
                getFruit();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Object getFruit() throws InterruptedException {
        synchronized (pushLock){
            System.out.println("getFruit fruitBasket size = "+fruitBasket.size());
            if(fruitBasket.size() <= 0){
//                notifyProducer();
                pushLock.wait();
            }
            pushLock.notifyAll();
            return fruitBasket.pop();
        }
    }

    private void notifyProducer() {
        pushLock.notifyAll();
    }
}
