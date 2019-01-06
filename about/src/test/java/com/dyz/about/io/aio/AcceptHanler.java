package com.dyz.about.io.aio;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptHanler implements CompletionHandler<AsynchronousSocketChannel,AsyncServer > {

    @Override
    public void completed(AsynchronousSocketChannel result, AsyncServer attachment) {
        attachment.serverSocketChannel.accept(attachment, this);
        ByteBuffer buffer = ByteBuffer.allocate(10240);
        result.read(buffer, buffer, new ReadHandler(result));
    }

    @Override
    public void failed(Throwable exc, AsyncServer attachment) {

    }
}
