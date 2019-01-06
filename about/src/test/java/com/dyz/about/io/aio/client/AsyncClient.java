package com.dyz.about.io.aio.client;

import sun.nio.ch.BsdAsynchronousChannelProvider;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.concurrent.CountDownLatch;

public class AsyncClient implements Runnable {
    AsynchronousSocketChannel socketChannel;
    public static void main(String[] args) throws IOException {
        AsyncClient client = new AsyncClient();
        client.socketChannel = AsynchronousSocketChannel.open();
        new Thread(client).start();
//        BsdAsynchronousChannelProvider
    }
    @Override
    public void run() {
        CountDownLatch latch = new CountDownLatch(1);
        socketChannel.connect(new InetSocketAddress("localhost", 9090), socketChannel, new ConnectHandler());
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
