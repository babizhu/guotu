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
public class MessageDeCoderTest {


    @Test
    public void testDecoder() throws Exception {
//        String host = "localhost";
        String host = "120.77.222.248";
        int port = DefaultGuotuServer.PORT_DEFAULT;
//        final ExecutorService executorService = Executors.newCachedThreadPool();
//        for (int i = 0; i < 3; i++) {
//            executorService.execute(() -> {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
//            });
    }


//        executorService.awaitTermination(1000000, TimeUnit.DAYS);

//    }

    private class TestDecoderHandler extends ChannelInboundHandlerAdapter {
        private static final byte HEAD = (byte) 0xff;

        @Override
        public void channelActive(ChannelHandlerContext ctx) {
//            final Random random = new Random(100);
//            final byte[] data = new byte[1024 * 7];
//            ctx.channel().eventLoop().scheduleAtFixedRate(new Runnable() {
//                @Override
//                public void run() {
//                    final ByteBuf buffer = ctx.alloc().buffer();
//                    int lenFiledLength = 2;//长度字段本身所占的字节
////            byte bufLen = (byte) 129;
//                    byte cmdId = (byte) 12;
//                    buffer.writeByte(HEAD);
//                    buffer.writeShort(0);
//                    buffer.writeByte(cmdId);
//
//                    buffer.writeBytes(data);
//                    byte[] timeStamp = new byte[7];
//                    buffer.writeBytes(timeStamp);
//                    byte[] checkSum = new byte[2];
//                    buffer.writeBytes(checkSum);
//
//                    buffer.setShort(1, buffer.writerIndex() - 1 - lenFiledLength);// 1 for head,2 for len
//
//                    ctx.writeAndFlush(buffer);
//                }
//            }, 10, 100, TimeUnit.MILLISECONDS);
//FF 0A 02 88 99 00 00 00 01 99 88

//            FF头 0A 有10个字节  02 表示功能注册 88 99 00 00 01 99 88 中 00 00 00 01 表示第一个点

            byte byte1 = (byte) 0x88;
            byte byte2 = (byte) 0x99;
            final ByteBuf buffer = ctx.alloc().buffer();
//                    int lenFiledLength = 2;//长度字段本身所占的字节
//            byte bufLen = (byte) 129;
            byte cmdId = (byte) 2;
            buffer.writeByte(HEAD);
            buffer.writeByte(0);
            buffer.writeByte(cmdId);

            buffer.writeByte(byte1);
            buffer.writeByte(byte2);
            buffer.writeInt(19);//clientID
            buffer.writeByte(byte2);
            buffer.writeByte(byte1);

            byte[] timeStamp = new byte[7];
            buffer.writeBytes(timeStamp);
            byte[] checkSum = new byte[2];
            buffer.writeBytes(checkSum);

            buffer.setByte(1, buffer.writerIndex() - 1);// 1 for head,2 for len

            ctx.writeAndFlush(buffer);
        }

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) {
            ByteBuf m = (ByteBuf) msg; // (1)
            System.out.println(m.toString(Charset.defaultCharset()));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }
}