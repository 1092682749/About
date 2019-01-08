package com.dyz.about.netty.handler;

import com.dyz.about.netty.MessageFormat;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

import java.util.Date;

public class HttpHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("有连接过来");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            WebSocketServerHandshakerFactory handshakerFactory = new WebSocketServerHandshakerFactory(
                    "ws:localhost:9090", null, true
            );
            WebSocketServerHandshaker handshaker = handshakerFactory.newHandshaker((HttpRequest) msg);
            handshaker.handshake(ctx.channel(), (FullHttpRequest) msg);
        }
        if(msg instanceof TextWebSocketFrame) {
            TextWebSocketFrame frame = (TextWebSocketFrame) msg;
            System.out.println(frame.text());
            MessageFormat format = new MessageFormat();
            format.setContent("asd");
            format.setTime(new Date());
            ctx.channel().writeAndFlush(new TextWebSocketFrame("ye ok"));
        } else {
            ctx.channel().writeAndFlush(new TextWebSocketFrame("ye ok"));
        }
    }
}
