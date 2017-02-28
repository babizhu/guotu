package org.bbz.srxk.guotu.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.bbz.srxk.guotu.client.Client;
import org.bbz.srxk.guotu.client.ClientsInfo;
import org.bbz.srxk.guotu.handler.cmd.Cmd;
import org.bbz.srxk.guotu.handler.cmd.LoginCmd;
import org.bbz.srxk.guotu.handler.cmd.RainfallCmd;
import org.bbz.srxk.guotu.handler.codec.MessageContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liulaoye on 17-2-20.
 * 真正的业务核心处理
 */
public class ProcessDispatcher extends SimpleChannelInboundHandler<MessageContainer>{
    private static final Logger LOG = LoggerFactory.getLogger( ProcessDispatcher.class );


    @Override
    protected void channelRead0( ChannelHandlerContext ctx, MessageContainer container ) throws Exception{
        dispacher( container, ctx );
    }

//    @Override
//    public void channelActive( ChannelHandlerContext ctx ) throws Exception{
//        try {
////            ctx.channel().eventLoop().scheduleAtFixedRate( new Runnable(){
////                private  int count=0;
////
////                @Override
////                public void run(){
////                    ctx.writeAndFlush( "hello "+ctx.channel().remoteAddress()+" the count is " + count++ );
////                }
////            }, 1, 1, TimeUnit.SECONDS );
//
//        } finally {
//
//            super.channelActive( ctx );
//        }
//    }

    /**
     * 对输入命令进行统一调度
     *
     * @param container 命令容器
     * @param ctx   ctx
     */
    private void dispacher( MessageContainer container, ChannelHandlerContext ctx ) throws InterruptedException{

        LOG.debug( container.toString() );

        final short cmdId = container.getCmdId();
        Cmd cmd = Cmd.fromNum( cmdId );
        final ClientsInfo clientsInfo = ClientsInfo.INSTANCE;
        final Client client = clientsInfo.getClient( ctx );
        if( client == null && cmd != Cmd.LOGIN_CMD ) {
            LOG.debug( "用户未登录，上传的信息为: " + container.toString() );
            clientsInfo.remove( ctx );
            return;
        }

        Thread.sleep( 20 );//It is not a joke，前端说后台回复太快，反应不过来需要降速 :-(
        ByteBuf response;
        switch( cmd ) {
            case LOGIN_CMD:
                response = new LoginCmd( ctx, container.getData() ).run( null );
                break;
            case RAINFALL:
                response = new RainfallCmd( ctx, container.getData() ).run( client );
                break;
            default:

                response = ctx.alloc().buffer();
                response.writeByte( 0xff );
                response.writeByte( 0x55 );
                response.writeByte( 0x88 );
                response.writeByte( 0x88 );
                response.writeByte( 0x88 );

                LOG.debug( "Command (" + cmdId + ") not found!!!" );

        }

        LOG.debug( "reqeust:" + container + ", response:" + ByteBufUtil.hexDump( response ));
        LOG.debug( "===========================================================================");

        if( response.writerIndex() > 0){
            ctx.writeAndFlush( response );

        }
    }


}
