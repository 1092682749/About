package com.dyz.about.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

//@WebListener
public class NettyServer implements ServletContextListener, Runnable {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        new Thread(this).start();
        System.out.println("######线程已启动");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void run() {
        EventLoopGroup work = new NioEventLoopGroup();
        EventLoopGroup boos = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(boos, work)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new MyChannelInitializer())
                    .option(ChannelOption.SO_BACKLOG, 128);
//                    .childOption(ChannelOption.TCP_NODELAY, true);
            try {
                ChannelFuture future = bootstrap.bind(9090).sync();
                System.out.println("监听端口9090");
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }finally {
                work.shutdownGracefully();
                boos.shutdownGracefully();
        }
    }
    public static void main(String[] args) {
        new NettyServer().run();
    }
}
