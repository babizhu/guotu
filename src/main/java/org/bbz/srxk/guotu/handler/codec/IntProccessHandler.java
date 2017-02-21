package org.bbz.srxk.guotu.handler.codec;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liulaoye on 17-2-20.
 * 用于测试编解码
 */
public class IntProccessHandler extends ChannelInboundHandlerAdapter{
    private static final Logger LOG = LoggerFactory.getLogger( IntProccessHandler.class );


    @Override
    public void channelRead( ChannelHandlerContext ctx, Object msg ) throws Exception{
        try {
            Integer i = (Integer) msg;
           LOG.debug( i+"" );
        } finally {

            super.channelRead( ctx, msg );
        }
    }
}