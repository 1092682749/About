package com.dyz.about.io.aio.client;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ClientReadHandler implements CompletionHandler<Integer, ByteBuffer> {
    AsynchronousSocketChannel channel;
    ClientReadHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        System.out.println("read completion");
        attachment.flip();
        byte[] bytes = new byte[attachment.remaining()];
        attachment.get(bytes);
        try {
            System.out.println(new String(bytes, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        attachment.clear();
        channel.read(attachment,attachment, this);

    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
