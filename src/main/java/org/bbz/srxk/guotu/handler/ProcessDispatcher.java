package org.bbz.srxk.guotu.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.Attribute;
import org.bbz.srxk.guotu.client.Client;
import org.bbz.srxk.guotu.client.ClientsInfo;
import org.bbz.srxk.guotu.handler.cmd.Cmd;
import org.bbz.srxk.guotu.handler.cmd.CtrlClientCmd;
import org.bbz.srxk.guotu.handler.cmd.LoginCmd;
import org.bbz.srxk.guotu.handler.cmd.RainfallCmd;
import org.bbz.srxk.guotu.handler.codec.MessageContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.bbz.srxk.guotu.handler.cmd.LoginCmd.ATTR_ID_KEY;

/**
 * Created by liulaoye on 17-2-20.
 * 真正的业务核心处理
 */
public class ProcessDispatcher extends SimpleChannelInboundHandler<MessageContainer>{
    public static final Logger LOG = LoggerFactory.getLogger( ProcessDispatcher.class );

    //    Attribute<Integer> ATTR_ID_KEY = ctx.attr(NETTY_CHANNEL_KEY);

    @Override
    public void channelActive( ChannelHandlerContext ctx ) throws Exception{


    }

    @Override
    protected void channelRead0( ChannelHandlerContext ctx, MessageContainer container ) throws Exception{
        try {

            dispacher( container, ctx );
        } finally {
            container.getData().release();
        }
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
     * @param ctx       ctx
     */
    private void dispacher( MessageContainer container, ChannelHandlerContext ctx ) throws InterruptedException{

        LOG.debug( container.toString() );

        final Client client = getClientByCtx( ctx );

        if( !cmdIsValid( container ) ) {
            if( client != null ) {
                ClientsInfo.INSTANCE.remove( client.getClientId() );
            } else {
                ctx.close();
            }

            LOG.debug( "head or checksum error: head is " + container.getHead() + ",checksum is " + Arrays.toString( container.getCheckSum() ) );
            return;
        }

        final short cmdId = container.getCmdId();
        Cmd cmd = Cmd.fromNum( cmdId );


        if( client == null && cmd != Cmd.LOGIN_CMD ) {
            LOG.debug( "user NOT login, the cmd is: " + container.toString() );
//            clientsInfo.remove( ctx );
            return;
        }

        Thread.sleep( 20 );//It is not a joke，前端说后台回复太快，反应不过来需要降速 :-(
        ByteBuf response = null;
        switch( cmd ) {
            case LOGIN_CMD:
                response = new LoginCmd( ctx, container.getData() ).run( null );
                break;
            case RAINFALL_CMD:
                response = new RainfallCmd( ctx, container.getData() ).run( client );
                break;
            case CTRL_CMD:
                new CtrlClientCmd( ctx, container.getData() ).run( client );
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

        LOG.debug( "reqeust:" + container + ", response:" +
                (response == null ? "" : ByteBufUtil.hexDump( response )) );
        LOG.debug( "===========================================================================" );

        if( response != null ) {
            ctx.writeAndFlush( response );
        }

    }

    /**
     * 获取此链接关联的clientId
     *
     * @param ctx ctx
     * @return client
     */
    private Client getClientByCtx( ChannelHandlerContext ctx ){
        if( ctx.channel().hasAttr( ATTR_ID_KEY ) ) {
            final Attribute<Integer> attr = ctx.channel().attr( ATTR_ID_KEY );
            if( attr != null ) {
                return ClientsInfo.INSTANCE.getClient( attr.get() );

            }
        }
        return null;
    }

    /**
     * 检测命令的有效性，包括
     * 包头
     * 验证码
     *
     * @param container MessageContainer
     * @return valid for true
     */
    private boolean cmdIsValid( MessageContainer container ){
        return container.getHead() == (byte) 0xff;
    }


}
