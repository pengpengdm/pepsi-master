package com.pepsi.interview.thread.ConsumerProducer;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/21
 * describe:
 */
public class Launch {

    public static void main(String[] args) {
        Storage<IPhone> storage = new Storage<>();
        new Thread(new Procuder(storage)).start();
        new Thread(new Consumer(storage)).start();

    }

}
