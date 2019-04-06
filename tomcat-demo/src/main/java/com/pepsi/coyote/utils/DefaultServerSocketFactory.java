package com.pepsi.coyote.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;


public final class DefaultServerSocketFactory implements ServerSocketFactory{
    @Override
    public ServerSocket createSocket(int port) throws IOException {
        return (new ServerSocket(port));
    }

    @Override
    public ServerSocket createSocket(int port, int backlog) throws IOException {
        return (new ServerSocket(port, backlog));
    }

    @Override
    public ServerSocket createSocket(int port, int backlog, InetAddress ifAddress) throws IOException {
        return (new ServerSocket(port, backlog, ifAddress));
    }
}
