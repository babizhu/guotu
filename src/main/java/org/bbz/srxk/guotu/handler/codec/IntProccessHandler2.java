package org.bbz.srxk.guotu.handler.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liulaoye on 17-2-20.
 * 用于测试编解码
 */
public class IntProccessHandler2 extends SimpleChannelInboundHandler<Integer>{
    private static final Logger LOG = LoggerFactory.getLogger( IntProccessHandler2.class );

    @Override
    protected void channelRead0( ChannelHandlerContext ctx, Integer msg ) throws Exception{
        LOG.debug( msg.toString() );
//        msg.release();

    }
}