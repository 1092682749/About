package com.dyz.about.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 9090));
        socketChannel.configureBlocking(false);
        new Thread(new ClientSelectorHandler(socketChannel)).start();
    }
}
