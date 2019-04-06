package com.pepsi.interview.thread.ConsumerProducer;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/21
 * describe:
 */
public class Procuder implements Runnable {

    private Storage<IPhone> storage;

    public Procuder(Storage storage){
        this.storage = storage;
    }

    @Override
    public void run() {
        for(int i = 0;i<20;i++){
            storage.procuder(new IPhone(i,"version"+i));
            try {
                Thread.sleep(10);//每隔10毫秒生产一个产品
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
