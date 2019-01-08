package com.dyz.about.netty;


import com.dyz.about.netty.dncode.PrivateDncode;
import com.dyz.about.netty.encode.PrivateEncode;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Date;

public class NettyClient {
    public static void main(String[] args) {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.TCP_NODELAY, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ChannelInitializer<NioSocketChannel>() {
                        @Override
                        protected void initChannel(NioSocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new PrivateDncode(1024 * 1024, 4, 4));
                            ch.pipeline().addLast(new PrivateEncode());
                        }
                    });
                }
            });


            // Start the client.
            ChannelFuture f = b.connect("localhost", 9090).sync(); // (5)
            f.addListener(new ChannelFutureListener() {

                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    MessageFormat format = new MessageFormat();
                    format.setTime(new Date());
                    format.setContent("asd");
                    channelFuture.channel().writeAndFlush(format);
                }
            });
            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}

