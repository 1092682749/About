package com.dyz.about.config.netty.encode;

import com.dyz.about.config.netty.MessageFormat;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingEncoder;
import org.jboss.marshalling.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public class PrivateEncode extends MessageToByteEncoder<MessageFormat> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageFormat messageFormat, ByteBuf byteBuf) throws Exception {
        Marshaller marshaller = buildMarshalling();
        int pos = byteBuf.writerIndex();
        byteBuf.writeBytes(new byte[4]);
        ByteOutput output = new ByteOutput() {

            @Override
            public void write(int i) throws IOException {
                    byteBuf.writeInt(i);
            }

            @Override
            public void write(byte[] bytes) throws IOException {
                    byteBuf.writeBytes(bytes);
            }

            @Override
            public void write(byte[] bytes, int i, int i1) throws IOException {
                    byteBuf.writeBytes(bytes, i, i1);
            }

            @Override
            public void close() throws IOException {

            }

            @Override
            public void flush() throws IOException {

            }
        };
        marshaller.start(output);
        marshaller.writeObject(messageFormat.getTime());
        marshaller.finish();
        byteBuf.setInt(pos, byteBuf.writerIndex() - pos - 4);
        byteBuf.writeBytes(messageFormat.getContent().getBytes());
    }
    protected static Marshaller buildMarshalling() throws IOException {
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        Marshaller marshaller = marshallerFactory.createMarshaller(configuration);
        return marshaller;
    }
}
