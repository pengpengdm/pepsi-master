package com.pepsi.interview.thread.aqs;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/01/22
 * describe:
 */

public class Node {
    volatile int waitStatus;
    private Node prev;
    private Node next;
    volatile Thread thread;
    //.....省略
}
