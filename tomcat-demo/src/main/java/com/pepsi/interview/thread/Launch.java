package com.pepsi.interview.thread;

import java.io.IOException;
import java.util.Stack;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/21
 * describe:
 */
public class Launch {

    public static void main(String[] args) throws IOException {
        Stack fruitBasket = new Stack();
        Object pushLuck = new Object();
        Object popLuck = new Object();

        Producer producer = new Producer(fruitBasket,popLuck,pushLuck);
        Thread thread = new Thread(producer);

        Consumer consumer = new Consumer(fruitBasket,popLuck,pushLuck);
        Thread threadConsumer = new Thread(consumer);

        thread.start();
        threadConsumer.start();

        System.in.read();
    }




}
