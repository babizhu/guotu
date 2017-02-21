package org.bbz.srxk.guotu.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by liulaoye on 17-2-20.
 * 真正的业务核心处理
 */
public class ProcessHandler extends SimpleChannelInboundHandler<ByteBuf>{
    private static final Logger LOG = LoggerFactory.getLogger( ProcessHandler.class );

    @Override
    protected void channelRead0( ChannelHandlerContext ctx, ByteBuf msg ) throws Exception{
        LOG.debug( ByteBufUtil.hexDump( msg ) );
//        msg.release();

    }

    private int count = 0;
    @Override
    public void channelActive( ChannelHandlerContext ctx ) throws Exception{
        try {
            ctx.channel().eventLoop().scheduleAtFixedRate( new Runnable(){
                private  int count=0;

                @Override
                public void run(){
                    ctx.writeAndFlush( "hello "+ctx.channel().remoteAddress()+" the count is " + count++ );
                }
            }, 1, 1, TimeUnit.SECONDS );

        } finally {

            super.channelActive( ctx );
        }
    }

}
