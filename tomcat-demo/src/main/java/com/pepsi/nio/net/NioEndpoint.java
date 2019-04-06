package com.pepsi.nio.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.util.concurrent.DefaultThreadFactory;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.concurrent.ThreadFactory;

/**
 * @author pepsi
 * @version 1.0
 * @date 2018/12/26
 * describe:
 */
public class NioEndpoint implements Runnable {

    private volatile ServerSocketChannel serverSock = null;

    @Override
    public void run() {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //BOSS线程池
            EventLoopGroup bossLoopGroup = new NioEventLoopGroup(1);
            //WORK线程池：这样的申明方式，主要是为了向读者说明Netty的线程组是怎样工作的
            ThreadFactory threadFactory = new DefaultThreadFactory("work thread pool");
            int processorsNumber = Runtime.getRuntime().availableProcessors();
            EventLoopGroup workLoogGroup = new NioEventLoopGroup(processorsNumber * 2, threadFactory, SelectorProvider.provider());
            serverBootstrap.group(bossLoopGroup , workLoogGroup);
            //只能是实现了ServerChannel接口的“服务器”通道类
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ByteArrayEncoder());
                    ch.pipeline().addLast(new ByteArrayDecoder());

                }
            });

            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
            serverBootstrap.bind(new InetSocketAddress("127.0.0.1", 8080));
        }catch (Exception e){

        }
    }


    public void start(){



    }
}
