package com.pepsi.collection.util;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/02/26
 * describe:
 */
public class DequeImpl<E> implements Deque<E>{


    @Override
    public boolean add(E e) {
        return false;
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    @Override
    public E remove() {
        return null;
    }

    @Override
    public E poll() {
        return null;
    }

    @Override
    public E element() {
        return null;
    }

    @Override
    public E peek() {
        return null;
    }
}
