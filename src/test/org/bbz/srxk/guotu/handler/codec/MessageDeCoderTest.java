package org.bbz.srxk.guotu.handler.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.bbz.srxk.guotu.handler.cmd.Cmd;
import org.junit.Assert;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;
import static org.bbz.srxk.guotu.server.DefaultGuotuServer.PORT_DEFAULT;

/**
 * Created by liulaoye on 17-2-20.
 * test
 */
public class MessageDeCoderTest{

    @Test

    public void testFramesDecoded(){
        int cmdId = 109;
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte( 0xff );//head
        buf.writeByte( 10 );//length
        buf.writeByte( cmdId );//cmdId
        for( int i = 0; i < 8; i++ ) {
            buf.writeByte( i );
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel( new MessageDecoder() );


        Assert.assertTrue( channel.writeInbound( input ) );
        Assert.assertTrue( channel.finish() );

        final MessageContainer o = channel.readInbound();
        System.out.println(o);
        Assert.assertEquals( cmdId, o.getCmdId() );

        Assert.assertNull( channel.readInbound() );

    }

    @Test
    public void testFramesDecoded2() {
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++) {
            buf.writeByte(i);
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel(new MessageDecoder());
        Assert.assertFalse(channel.writeInbound(input.readBytes(2)));
        Assert.assertTrue(channel.writeInbound(input.readBytes(7)));
        Assert.assertTrue(channel.finish());

        Assert.assertEquals(buf.readBytes(3), channel.readInbound());
        Assert.assertEquals(buf.readBytes(3), channel.readInbound());
        Assert.assertEquals(buf.readBytes(3), channel.readInbound());
        Assert.assertNull(channel.readInbound());
    }

    @Test
    public void testDecoder() throws Exception{
        String host = "localhost";
//        String host = "120.77.222.248";
        int port = PORT_DEFAULT;
//        final ExecutorService executorService = Executors.newCachedThreadPool();
//        for (int i = 0; i < 3; i++) {
//            executorService.execute(() -> {
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group( workerGroup ); // (2)
            b.channel( NioSocketChannel.class ); // (3)
            b.option( ChannelOption.SO_KEEPALIVE, true ); // (4)
            b.handler( new ChannelInitializer<SocketChannel>(){
                @Override
                public void initChannel( SocketChannel ch ) throws Exception{
                    ch.pipeline().addLast( new TestDecoderHandler() );
                }
            } );

            // Start the client.
            ChannelFuture f = b.connect( host, port ).sync(); // (5)

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch( InterruptedException e ) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
//            });
    }


//        executorService.awaitTermination(1000000, TimeUnit.DAYS);

//    }

    private static String formatByteBuf( ChannelHandlerContext ctx, String eventName, ByteBuf msg ){
        String chStr = ctx.channel().toString();
        int length = msg.readableBytes();
        if( length == 0 ) {
            StringBuilder buf = new StringBuilder( chStr.length() + 1 + eventName.length() + 4 );
            buf.append( chStr ).append( ' ' ).append( eventName ).append( ": 0B" );
            return buf.toString();
        } else {
            int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
            StringBuilder buf = new StringBuilder( chStr.length() + 1 + eventName.length() + 2 + 10 + 1 + 2 + rows * 80 );

            buf.append( chStr ).append( ' ' ).append( eventName ).append( ": " ).append( length ).append( 'B' ).append( NEWLINE );
            appendPrettyHexDump( buf, msg );

            return buf.toString();
        }
    }

private class TestDecoderHandler extends ChannelInboundHandlerAdapter{
    private static final byte HEAD = (byte) 0xff;

    @Override
    public void channelActive( ChannelHandlerContext ctx ){
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


        ctx.writeAndFlush( buildCmd( ctx.alloc().buffer(), Cmd.UNDEFINED ) );
        ctx.writeAndFlush( buildCmd( ctx.alloc().buffer(), Cmd.LOGIN_CMD ) );
        ctx.writeAndFlush( buildCmd( ctx.alloc().buffer(), Cmd.RAINFALL ) );
        ctx.writeAndFlush( buildCmd( ctx.alloc().buffer(), Cmd.UNDEFINED ) );
    }


    ByteBuf buildCmd( ByteBuf buffer, Cmd cmd ){
        buffer.writeByte( HEAD );
        buffer.writeByte( 0 );//长度
        if( cmd == Cmd.UNDEFINED ) {
            buffer.writeByte( 244 );//故意发送一个错误的cmdId

        } else {
            buffer.writeByte( (byte) cmd.toNum() );
        }
        switch( cmd ) {
            case RAINFALL:
                buildRainfallCmd( buffer );
                break;
            case LOGIN_CMD:
                buildLoginCmd( buffer );
                break;
            default:
                System.out.println( "未找到的命令:" + cmd );
        }

        byte[] checkSum = new byte[2];
        buffer.writeBytes( checkSum );

        buffer.setByte( 1, buffer.writerIndex() - 1 );// 1 for head
        return buffer;
    }

    void buildRainfallCmd( ByteBuf buffer ){
        buffer.writeByte( 0x03 );
        buffer.writeByte( 0x04 );
        buffer.writeByte( 0x00 );
        buffer.writeByte( 0x00 );
        buffer.writeShort( 1334 );
        buffer.writeByte( 0xfa );
        buffer.writeByte( 0x33 );

        /**********************时间戳*************************/
        Calendar cal = Calendar.getInstance();
        cal.setTime( new Date() );
        buffer.writeByte( cal.get( Calendar.HOUR_OF_DAY ) );//时
        buffer.writeByte( cal.get( Calendar.MINUTE ) );//分
        buffer.writeByte( cal.get( Calendar.SECOND ) );//秒

        buffer.writeByte( cal.get( Calendar.YEAR ) % 2000 );//年
        buffer.writeByte( cal.get( Calendar.MONTH ) + 1 );//月
        buffer.writeByte( cal.get( Calendar.DAY_OF_MONTH ) );//日
        int dayOfWeek = cal.get( Calendar.DAY_OF_WEEK );
        dayOfWeek = dayOfWeek == 1 ? 7 : dayOfWeek - 1;
        buffer.writeByte( dayOfWeek );//星期
        /**********************时间戳*************************/

    }

    void buildLoginCmd( ByteBuf buffer ){
        byte byte1 = (byte) 0x88;
        byte byte2 = (byte) 0x99;
        buffer.writeByte( byte1 );
        buffer.writeByte( byte2 );
        buffer.writeInt( 19 );//clientID
        buffer.writeByte( byte2 );
        buffer.writeByte( byte1 );

    }

    @Override
    public void channelRead( ChannelHandlerContext ctx, Object msg ){
        System.out.println( "TestDecoderHandler.channelRead" );
        ByteBuf m = (ByteBuf) msg; // (1)
        System.out.println( MessageDeCoderTest.formatByteBuf( ctx, "read", m ) );
    }

    @Override
    public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ){
        cause.printStackTrace();
        ctx.close();
    }
}
}