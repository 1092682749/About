package com.dyz.about.io.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class WriteHandler implements CompletionHandler<Integer, ByteBuffer> {
    AsynchronousSocketChannel channel;
    public WriteHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        if (attachment.hasRemaining()) {
            channel.write(attachment, attachment, this);
        }
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {

    }
}
