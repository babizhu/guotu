package org.bbz.srxk.guotu.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.junit.Test;

import static org.bbz.srxk.guotu.server.DefaultGuotuServer.PORT_DEFAULT;

public class GongdiMessageDecoderTest extends BaseClient{

    private static final byte HEAD = (byte) 0XFF;

    @Test
    public void connctTest() throws Exception{
//        String host = "localhost";
        String host = "121.43.182.176";

        org.bbz.srxk.guotu.handler.codec.GongdiMessageDecoderTest.TestHandler handlerAdapter = new org.bbz.srxk.guotu.handler.codec.GongdiMessageDecoderTest.TestHandler();
        new ClientTest().connect( host, PORT_DEFAULT, handlerAdapter );
    }

    private enum Cmd{
        ENTER,//进入某区域
        EXIT, //离开某区域
        ALERT, //报警
        KAOQING,//考勤

    }

    private class TestHandler extends ChannelInboundHandlerAdapter{

        @Override
        public void channelActive( ChannelHandlerContext ctx ){
            ctx.writeAndFlush( buildCmd( ctx.alloc().buffer(), Cmd.EXIT ) );
//            ctx.writeAndFlush( buildCmd( ctx.alloc().buffer(), Cmd.ENTER ) );
//            ctx.writeAndFlush( buildCmd( ctx.alloc().buffer(), Cmd.EXIT ) );
            ctx.writeAndFlush( buildCmd( ctx.alloc().buffer(), Cmd.KAOQING ) );
//            ctx.writeAndFlush( buildCmd( ctx.alloc().buffer(), Cmd.ALERT ) );
        }

        @Override
        public void channelRead( ChannelHandlerContext ctx, Object msg ){
            System.out.println( formatByteBuf( ctx, "read", (ByteBuf) msg ) );
        }

        @Override
        public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ){
            cause.printStackTrace();
            ctx.close();
        }
    }

    ByteBuf buildCmd( ByteBuf buffer, org.bbz.srxk.guotu.handler.codec.GongdiMessageDecoderTest.Cmd cmd ){
        buffer.writeByte( HEAD );
        buffer.writeByte( 0 );//长度
        byte[] baseStationId = {0x1A,0x22,0x44,0x22,0x04};
        byte[] keyId = {0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11, 0x11};
        buffer.writeBytes( baseStationId );
        switch( cmd ) {
            case ENTER:
                buffer.writeByte( 0xb5 );
                break;
            case EXIT:
                buffer.writeByte( 0xb4 );
                break;
            case KAOQING:
                buffer.writeByte( 0xb3 );
                break;

        }
        buffer.writeBytes( keyId );
        buffer.setByte( 1, buffer.writerIndex() - 1 );// 1 for head
        return buffer;

    }
}



