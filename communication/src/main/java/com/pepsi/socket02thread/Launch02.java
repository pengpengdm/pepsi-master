package com.pepsi.socket02thread;

import java.util.concurrent.CountDownLatch;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/10/04
 * describe:
 */
public class Launch02 {

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(20);
        for (int i = 0; i < 20; i++,latch.countDown()) {
            SocketClient02 client02 = new SocketClient02(latch,i);
            new Thread(client02).start();
        }
    }
}
