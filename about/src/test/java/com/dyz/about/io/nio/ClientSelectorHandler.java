package com.dyz.about.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@SuppressWarnings("Duplicates")
public class ClientSelectorHandler implements Runnable {
    SocketChannel socketChannel;
    Selector selector;

    ClientSelectorHandler(SocketChannel socketChannel) throws IOException {
        this.socketChannel = socketChannel;
        selector = Selector.open();
        if (this.socketChannel.isConnected()) {
            this.socketChannel.register(selector, SelectionKey.OP_READ);
            ByteBuffer requestBuffer = ByteBuffer.allocate(10240);
            requestBuffer.put("这是一个请求".getBytes("utf-8"));
            requestBuffer.flip();
            this.socketChannel.write(requestBuffer);
        } else {
            socketChannel.register(selector, SelectionKey.OP_CONNECT);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeySet.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    handlerKey(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handlerKey(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        if (!key.isValid()) {
            return;
        }
        if (key.isConnectable()) {
            if (socketChannel.finishConnect()) {
                socketChannel.register(selector, SelectionKey.OP_READ);
            }
        }
        if (key.isReadable()) {
            ByteBuffer buffer = ByteBuffer.allocate(10240);
            int readState = socketChannel.read(buffer);
            if (readState > 0) {
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                String response = new String(bytes, "utf-8");
                System.out.println(response);
            }
        }

    }
}
