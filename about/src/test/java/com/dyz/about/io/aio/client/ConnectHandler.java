package com.dyz.about.io.aio.client;

import com.dyz.about.io.aio.WriteHandler;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class ConnectHandler implements CompletionHandler<Void, AsynchronousSocketChannel> {
    @Override
    public void completed(Void result, AsynchronousSocketChannel attachment) {
        ByteBuffer buffer = ByteBuffer.allocate(10240);
        try {
//            buffer.put("这是一个请求".getBytes());
//            buffer.flip();
//            attachment.write(buffer, buffer, new WriteHandler(attachment));
            new HeartTask(attachment).work();
            ByteBuffer resp = ByteBuffer.allocate(10240);
            attachment.read(resp, resp, new ClientReadHandler(attachment));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {

    }
    class HeartTask {
        AsynchronousSocketChannel channel;
        HeartTask(AsynchronousSocketChannel channel) {
            this.channel = channel;
        }
        public void work() {
            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    buffer.put(new Date().toString().getBytes());
                    buffer.flip();
                    channel.write(buffer, buffer, new WriteHandler(channel));
                }
            }, 300,3000);
        }
    }
}
