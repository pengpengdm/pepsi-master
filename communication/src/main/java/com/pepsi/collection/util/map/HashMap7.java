package com.pepsi.collection.util.map;

import java.util.HashMap;

/**
 * @author pepsi
 * @version 1.0
 * @date 2019/03/02
 * describe: 1.7版本
 */
public class HashMap7<K,V> {

    static final int DEFAULT_INITIAL_CAPACITY = 1<<4; //默认16位

    transient Node<K,V>[] table;


    class Node<K,V> {
        K key;
        V value;
        private int hash;
        private Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next){
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }

    public void put(K key,V value){
        if(table == null ) table = new Node[DEFAULT_INITIAL_CAPACITY];
    }

}
