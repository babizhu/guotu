package org.bbz.srxk.guotu.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.bbz.srxk.guotu.handler.cmd.GongdiCmd;
import org.bbz.srxk.guotu.handler.cmd.GongdiSendCmd;
import org.bbz.srxk.guotu.handler.codec.GongdiMessageContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by liulaoye on 17-2-20.
 * 真正的业务核心处理
 */
public class GongdiProcessDispatcher extends SimpleChannelInboundHandler<GongdiMessageContainer>{
    private static final Logger LOG = LoggerFactory.getLogger( GongdiProcessDispatcher.class );

    private ConcurrentHashMap<String, Integer> exitMap = new ConcurrentHashMap<>();

    private int i= 1;
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

//        LOG.error( Thread.currentThread().getName() );
//        LOG.debug( container.toString() );
        if(container.getHead() != -1){
            LOG.error( "包头不等于0XFF，关闭链接！" );
            ctx.close();
            return;
        }
        final GongdiSendCmd gongdiSendCmd = new GongdiSendCmd( ctx, container.getData() );

//        if( gongdiSendCmd.getCmdId() == GongdiCmd.ENTER.toNum() && xx.contains( gongdiSendCmd.buildExitKey() ) ) {
//            gongdiSendCmd.sendAlertCmd();
//            deletePendingStatus( gongdiSendCmd.buildExitKey() );
//            return;
//        }
//
//        if( gongdiSendCmd.getCmdId() == GongdiCmd.EXIT.toNum() && !xx.contains( gongdiSendCmd.buildExitKey() ) ) {
//            xx.add( gongdiSendCmd.buildExitKey() );
//            ctx.executor().schedule( () -> {
//                LOG.info( Thread.currentThread().getName() );
//                if( xx.contains( gongdiSendCmd.buildExitKey() ) ) {
//                    gongdiSendCmd.run();
//                    deletePendingStatus( gongdiSendCmd.buildExitKey() );
//                }
//            }, 1, TimeUnit.SECONDS );
//            return;
//        }
        final String exitKey = gongdiSendCmd.buildExitKey();
        if( gongdiSendCmd.getCmdId() == GongdiCmd.ENTER.toNum() ) {
            if( exitMap.remove( exitKey ) != null ) {
                gongdiSendCmd.sendAlertCmd();
                return;
            }
        }
        if( gongdiSendCmd.getCmdId() == GongdiCmd.EXIT.toNum() ) {
            exitMap.computeIfAbsent( exitKey, res -> {
                ctx.executor().schedule( () -> {
                    LOG.info( Thread.currentThread().getName() );
                    if( exitMap.remove( exitKey ) != null ) {
                        gongdiSendCmd.run();
                    }

                }, 1, TimeUnit.SECONDS );
                return new Integer(i++);
            } );
           return;
        }
        gongdiSendCmd.run();
    }
//
//
//    private void deletePendingStatus( String key ){
//        xx.remove( key );
//    }


}
