package org.bbz.srxk.guotu.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.bbz.srxk.Launcher;
import org.bbz.srxk.guotu.client.HardwareClientsInfo;
import org.bbz.srxk.guotu.handler.cmd.Cmd;
import org.junit.Test;

import java.util.concurrent.ArrayBlockingQueue;

import static org.bbz.srxk.guotu.server.DefaultGuotuServer.PORT_DEFAULT;

/**
 * Created by liulaoye on 17-2-20.
 * <p>
 * 模拟一个web客户端向雨量采集器客户端发送控制指令，数据流向如下：
 * web -> this ->client
 * client -> this -> web
 * </p>
 * 考虑这个往返的过程比较复杂，要多进行测试
 */
public class WebClientResponseTest extends BaseClient{


    @Test
    public void connctTest() throws Exception{
        startServer();
        TestHandler handlerAdapter = new TestHandler();
        new WebClientResponseTest().connect( "localhost", PORT_DEFAULT, handlerAdapter );

    }

    /**
     * 在额外的线程启动一个服务器
     */
    private void startServer(){
        new Thread( () -> {
            Launcher.main();
            try {
                Thread.sleep( 1000 );//等待客户端正常登录
            } catch( InterruptedException e ) {
                e.printStackTrace();
            }

            final int clientId =  19;
            System.out.println( "模拟web服务器收到了客户端网页发送的操作指令,发送控制指令给客户端，参数为999");
            final ArrayBlockingQueue queue = (ArrayBlockingQueue) HardwareClientsInfo.INSTANCE.sendCtrlCmd( clientId,  "999" );
            if( queue != null ) {
                try {

                    System.out.println( "客户端对于控制指令的返回结果是:" + queue.take() );

                } catch( InterruptedException e ) {
                    e.printStackTrace();
                }
            }else {
                System.err.println( clientId +" 硬件客户端不在线！" );
            }
        } ).start();
    }


    private class TestHandler extends ChannelInboundHandlerAdapter{

        @Override
        public void channelActive( ChannelHandlerContext ctx ){
            ctx.writeAndFlush( buildCmd( ctx.alloc().buffer(), Cmd.LOGIN_CMD ) );
        }

        @Override
        public void channelRead( ChannelHandlerContext ctx, Object msg ){
            final ByteBuf m = (ByteBuf) msg;
            if( m.readableBytes() >= 4 ){
                int result = m.readInt();
                if( result == 999){
                    System.out.println( "模拟的硬件客户端收到了从服务器发送的控制指令");
                    //doSomething.......
                    ctx.writeAndFlush( buildCmd( ctx.alloc().buffer(),Cmd.CTRL_CMD) );//返回给服务器控制指令专用包
                }
            }

//            从中可以听到（联想）到《三寸天堂》《大雨将至》《海角七号》《月光》张信哲的《白月光》《信仰》翻唱 松隆子的 梦的点滴，石进的《街道的寂寞》。。《假如爱有天意》。。陈琳的《雨夜》等一些悲伤音乐

        }

        @Override
        public void exceptionCaught( ChannelHandlerContext ctx, Throwable cause ){
            cause.printStackTrace();
            ctx.close();
        }
    }

}