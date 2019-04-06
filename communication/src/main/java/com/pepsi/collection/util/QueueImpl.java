package com.pepsi.collection.util;

import java.util.NoSuchElementException;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/02/26
 * describe:
 */
public class QueueImpl<E> implements Queue<E> {

    transient int size = 0;

    transient Node header;

    transient Node tail;



    @Override
    public boolean add(E e){
        addTail(e);
        return true;
    }


    private boolean addTail(E e){
        final  Node<E> l = tail;
        final  Node<E> node = new Node<>(l,e,null);
        if(header==null)
            header = node;
        else
            l.next = node;
        tail = node;
        size++;
        return true;
    }

    @Override
    public boolean offer(E e) {
        return add(e);
    }


    @Override
    public E remove() {
        return removeHeader();
    }

    private E removeHeader() {
        final Node<E> f = header;
        if(f==null){
            throw new NoSuchElementException();
        }
        return unlinkFirst(f);
    }

    private E unlinkFirst(Node<E> f) {
        final E element = f.item;
        final Node<E> next = f.next;
        f.item = null;
        f.item = null;
        f.next = null; // help GC
        header = next;
        if (next == null)
            tail = null;
        else
            next.prev = null;
        size--;
        return element;
    }

    @Override
    public E poll() {
        final Node<E> f = header;
        return (f == null) ? null : unlinkFirst(f);
    }


    @Override
    public E element() {
        final Node<E> f = header;
        if (f == null)
            throw new NoSuchElementException();
        return f.item;
    }

    @Override
    public E peek() {
        final Node<E> f = header;
        return (f == null) ? null : f.item;
    }

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        Node(Node<E> prev, E element, Node<E> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
}
