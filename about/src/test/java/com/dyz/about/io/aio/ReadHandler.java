package com.dyz.about.io.aio;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    AsynchronousSocketChannel channel;
    ReadHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        System.out.println("read give integer is: " + result);
        attachment.flip();
        byte[] bytes = new byte[attachment.remaining()];
        attachment.get(bytes);
        try {
            String msg = new String(bytes, "utf-8");
            System.out.println(msg);
            ByteBuffer resp = ByteBuffer.allocate(10240);
            resp.put("收到了".getBytes("utf-8"));
            resp.flip();
            channel.write(resp, resp, new WriteHandler(channel));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        ByteBuffer buffer = ByteBuffer.allocate(10240);
        channel.read(buffer, buffer, this);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
