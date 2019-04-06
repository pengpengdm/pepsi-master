package com.pepsi.interview.thread.ConsumerProducer;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/21
 * describe:
 */
public class Consumer implements Runnable{

    private Storage storage;

    public Consumer(Storage storage){
        this.storage = storage;
    }

    @Override
    public void run() {
        for(int i = 0;i<20;i++){
            storage.consumer();
            try {
                Thread.sleep(100);//每隔100毫秒消费一个
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
