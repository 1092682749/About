package com.dyz.about.io.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("Duplicates")
public class SelectorHandler implements Runnable {
    ServerSocketChannel serverSocketChannel;
    Selector selector;

    SelectorHandler(ServerSocketChannel serverSocketChannel) throws IOException {
        this.serverSocketChannel = serverSocketChannel;
        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    @Override
    public void run() {
        while (true) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeySet = selector.selectedKeys();
                Iterator<SelectionKey> iterable = selectionKeySet.iterator();
                while (iterable.hasNext()) {
                    SelectionKey key = iterable.next();
                    iterable.remove();
                    handlerKey(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void handlerKey(SelectionKey key) throws IOException {
        if (key.isAcceptable()) {
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            SocketChannel sc = ssc.accept();
            // 不设置将会Connect reset by peer;
            sc.configureBlocking(false);
            sc.register(selector, SelectionKey.OP_READ);
            new TaskInTime(sc).run();
        }
        if (key.isReadable()) {
            SocketChannel sc = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(10240);
            int byteLen = sc.read(buffer);
            if (byteLen > 0) {
                buffer.flip();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                String request = new String(bytes, "utf-8");
                System.out.println(request);
                ByteBuffer responseBuffer = ByteBuffer.allocate(10240);
                responseBuffer.put("我已经收到了你的请求".getBytes());
                responseBuffer.flip();
                sc.write(responseBuffer);
            }
        }
    }

}
class TaskInTime {
    SocketChannel socketChannel;
    public TaskInTime(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }
    public void run() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    String msg = "现在时间" + new Date().toString();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    buffer.put(msg.getBytes());
                    buffer.flip();
                    socketChannel.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.scheduleAtFixedRate(timerTask, 2000, 10000);
    }
}
