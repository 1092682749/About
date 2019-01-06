package com.dyz.about.io.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AsyncServer implements Runnable {
    AsynchronousServerSocketChannel serverSocketChannel;
    public static void main(String[] args) throws IOException {
        AsyncServer server = new AsyncServer();
        server.serverSocketChannel = AsynchronousServerSocketChannel.open();
        server.serverSocketChannel.bind(new InetSocketAddress(9090));
        new Thread(server).start();
    }

    @Override
    public void run() {
        CountDownLatch latch = new CountDownLatch(1);
        serverSocketChannel.accept(this, new AcceptHanler());
        try {
            latch.await();
            serverSocketChannel.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
