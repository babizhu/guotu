package org.bbz.srxk.guotu.handler.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.bbz.srxk.guotu.server.DefaultGuotuServer;
import org.junit.Test;

import java.nio.charset.Charset;

/**
 * Created by liulaoye on 17-2-20.
 * test
 */
public class MessageDeCoderTest{

    @Test
    public void testDecoder() throws Exception{
        String host = "localhost";
        int port = DefaultGuotuServer.PORT_DEFAULT;
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option( ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new TestDecoderHandler());
                }
            });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync(); // (5)

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }

    }

    private class TestDecoderHandler extends ChannelInboundHandlerAdapter{
        private static final byte HEAD = 127;

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
            final ByteBuf buffer = ctx.alloc().buffer();
            byte bufLen = 4;
            buffer.writeByte( HEAD );
            buffer.writeByte( bufLen );
            buffer.writeInt( 100 );

            ctx.writeAndFlush(buffer);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf m = (ByteBuf) msg; // (1)
            System.out.println(m.toString( Charset.defaultCharset() ));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }
}