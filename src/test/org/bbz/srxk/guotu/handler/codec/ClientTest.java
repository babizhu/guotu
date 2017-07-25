package org.bbz.srxk.guotu.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.bbz.srxk.guotu.handler.cmd.Cmd;
import org.junit.Test;

import static org.bbz.srxk.guotu.server.DefaultGuotuServer.PORT_DEFAULT;

/**
 * Created by liulaoye on 17-2-20.
 * <p>
 * 最基本的测试：
 * 客户端连接后，分别发送4个包：
 * Cmd.UNDEFINED_CMD    由于尚未登录，服务器不会返回任何信息
 * Cmd.LOGIN_CMD        返回登录信息
 * Cmd.RAINFALL_CMD     返回ff 55 88 88 88  以及00 00 00 00 dd 00 01 00 01 发送雨量报警包，此包由于客户端原因，认为延时20ms
 * Cmd.UNDEFINED_CMD    返回ff 55 88 88 88
 *
 *        +----------------------------------------+
 |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
 +--------+-------------------------------------------------+----------------+
 |00000000| f0 bb 78 52 0b 21 24 11 03 0a 05 23 e8          |..xR.!$....#.   |
 +--------+-------------------------------------------------+----------------+
 [id: 0x34c83b8f, L:/192.168.0.105:50070 - R:/120.77.222.248:8080] read: 5B
 +-------------------------------------------------+
 |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
 +--------+-------------------------------------------------+----------------+
 |00000000| ff 55 88 88 88                                  |.U...           |
 +--------+-------------------------------------------------+----------------+
 [id: 0x34c83b8f, L:/192.168.0.105:50070 - R:/120.77.222.248:8080] read: 14B
 +-------------------------------------------------+
 |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
 +--------+-------------------------------------------------+----------------+
 |00000000| ff 55 88 88 88 00 00 00 00 dd 00 01 00 01       |.U............  |
 +--------+-------------------------------------------------+----------------+
 *
 * <p/>
 */
public class ClientTest extends BaseClient{

    @Test
    public void connctTest() throws Exception{
        String host = "localhost";
//        String host = "120.77.222.248";

        TestHandler handlerAdapter = new TestHandler();
        new ClientTest( ).connect(host, PORT_DEFAULT, handlerAdapter);
    }

    private class TestHandler extends ChannelInboundHandlerAdapter{

        @Override
        public void channelActive( ChannelHandlerContext ctx ){
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