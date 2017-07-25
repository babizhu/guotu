package org.bbz.srxk.guotu.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.bbz.srxk.guotu.handler.cmd.GongdiCmd;
import org.bbz.srxk.guotu.handler.cmd.GongdiSendCmd;
import org.bbz.srxk.guotu.handler.codec.GongdiMessageContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Created by liulaoye on 17-2-20.
 * 真正的业务核心处理
 */
public class GongdiProcessDispatcher extends SimpleChannelInboundHandler<GongdiMessageContainer>{
    private static final Logger LOG = LoggerFactory.getLogger( GongdiProcessDispatcher.class );
    Set<String> xx = Collections.synchronizedSet( new HashSet<>() );

    @Override
    protected void channelRead0( ChannelHandlerContext ctx, GongdiMessageContainer container ) throws Exception{
        try {
            dispacher( container, ctx );
        } finally {
            container.getData().release();
        }
    }

    /**
     * 对输入命令进行统一调度
     *
     * @param container 命令容器
     * @param ctx       ctx
     */
    private void dispacher( GongdiMessageContainer container, ChannelHandlerContext ctx ) throws InterruptedException{

        LOG.error( Thread.currentThread().getName() );
        LOG.debug( container.toString() );
        final GongdiSendCmd gongdiSendCmd = new GongdiSendCmd( ctx, container.getData() );

        if( gongdiSendCmd.getCmdId() == GongdiCmd.ENTER.toNum() && xx.contains( gongdiSendCmd.buildExitKey() ) ) {
            gongdiSendCmd.sendAlertCmd();
            deletePendingStatus( gongdiSendCmd.buildExitKey() );
            return;
        }

        if( gongdiSendCmd.getCmdId() == GongdiCmd.EXIT.toNum() && !xx.contains( gongdiSendCmd.buildExitKey() ) ) {
            xx.add( gongdiSendCmd.buildExitKey() );
            ctx.executor().schedule( () -> {
                LOG.info( Thread.currentThread().getName() );
                if( xx.contains( gongdiSendCmd.buildExitKey() ) ) {
                    gongdiSendCmd.run();
                    deletePendingStatus( gongdiSendCmd.buildExitKey() );
                }
            }, 1, TimeUnit.SECONDS );
            return;
        }
        gongdiSendCmd.run();
    }


    private void deletePendingStatus( String key ){
        xx.remove( key );
    }


}
