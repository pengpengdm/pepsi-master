package com.pepsi.interview.thread;

import java.util.Stack;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/21
 * describe:
 */
public class Producer implements Runnable {

    private Object pushLock;
    private Stack fruitBasket;
    private Object popLock;

    public Producer(Stack fruitBasket,Object pushLock,Object popLock){
        this.fruitBasket = fruitBasket;
        this.pushLock = pushLock;
        this.popLock = popLock;
    }

    @Override
    public void run() {
        while(true){
            try {
                putFruit();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void putFruit() throws InterruptedException {
        synchronized (pushLock){
            System.out.println("putFruit apple size ="+fruitBasket.size());
            if(fruitBasket.size()>=10){
                try {
//                    notifyConsumer();
                    pushLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            fruitBasket.push("pepsi");
            pushLock.notifyAll();
        }
    }

    private void notifyConsumer() {
        popLock.notifyAll();
    }


}
