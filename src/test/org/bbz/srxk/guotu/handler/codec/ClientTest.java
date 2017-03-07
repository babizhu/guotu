package org.bbz.srxk.guotu.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.bbz.srxk.guotu.handler.cmd.Cmd;
import org.junit.Test;

import static org.bbz.srxk.guotu.server.DefaultGuotuServer.PORT_DEFAULT;

/**
 * Created by liulaoye on 17-2-20.
 * test
 */
public class ClientTest extends BaseClient{



    @Test
    public void connctTest() throws Exception{
        TestHandler handlerAdapter = new TestHandler();
        new ClientTest( ).connect("localhost", PORT_DEFAULT, handlerAdapter);

    }



    private class TestHandler extends ChannelInboundHandlerAdapter{

        @Override
        public void channelActive( ChannelHandlerContext ctx ){
            System.out.println( "TestHandler.channelActive" );
            ctx.writeAndFlush( buildCmd( ctx.alloc().buffer(), Cmd.UNDEFINED_CMD ) );
            ctx.writeAndFlush( buildCmd( ctx.alloc().buffer(), Cmd.LOGIN_CMD ) );
            ctx.writeAndFlush( buildCmd( ctx.alloc().buffer(), Cmd.RAINFALL_CMD ) );
            ctx.writeAndFlush( buildCmd( ctx.alloc().buffer(), Cmd.UNDEFINED_CMD ) );
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
}