package com.dyz.about.io.nio;

import com.dyz.about.rpc.Inter;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

public class Server implements Inter, Serializable {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.socket().bind(new InetSocketAddress(9090));
        new Thread(new SelectorHandler(serverSocketChannel)).start();
    }

    @Override
    public void print() {
        System.out.println("my is server");
    }
}
