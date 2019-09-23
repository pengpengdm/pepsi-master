package com.pepsi.hbase;

public interface Command<T> {
    T execute(long timeout) throws Exception;
}
