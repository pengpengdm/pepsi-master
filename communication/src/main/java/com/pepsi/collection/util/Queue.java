package com.pepsi.collection.util;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/02/26
 * describe: 队列
 */
public interface Queue<E> {

    /***
     * 队列的实现类中 长度不一样，LinkedBlockQueue.队列满了添加报错。
     * @param e
     * @return
     */
    boolean add(E e);

    /***
     * 异常吃掉，返回false
     * @return
     */
    boolean offer(E e);

    /***
     * 移除头部的元 如果链表为空则报错。
     * @return
     */
    E remove();

    /***
     * 移除头部元素，和remove不同的是，链表为空返回null不报错。
     * @return
     */
    E poll();

    /***
     * 获取头部元素，但是不移除。为空跑出异常。
     * @return
     */
    E element();


    /***
     * 获取链表的头部元素，但是不移除。为空返回null
     * @return
     */
    E peek();



}
