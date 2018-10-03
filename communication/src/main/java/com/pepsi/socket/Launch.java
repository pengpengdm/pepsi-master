package com.pepsi.socket;

import java.util.concurrent.CountDownLatch;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/09/21
 * describe:
 */

public class Launch {
    public static void main(String[] args) throws InterruptedException {
        int countDownNum = 2;
        CountDownLatch countDownLatch = new CountDownLatch(countDownNum);

        for (int index = 0; index < countDownNum; index++,countDownLatch.countDown()) {
            SocketClient client = new SocketClient(countDownLatch,index);
            new Thread(client).start();
        }

        //只是为了保证守护线程在启动所有线程后，进入等待状态
        synchronized (Launch.class) {
            Launch.class.wait();
        }
    }

}
