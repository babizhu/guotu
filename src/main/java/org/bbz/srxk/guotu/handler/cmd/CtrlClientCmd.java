package org.bbz.srxk.guotu.handler.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.bbz.srxk.guotu.client.Client;
import org.bbz.srxk.guotu.client.HardwareClientsInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class CtrlClientCmd extends AbstractCmd{

    private static final Logger LOG = LoggerFactory.getLogger( CtrlClientCmd.class );
    private final ChannelHandlerContext ctx;

    /**
     * 由客户端返回的控制指令的响应信息
     */
    private int result;


    public CtrlClientCmd( ChannelHandlerContext ctx, ByteBuf data ){
        super( data );
        this.ctx = ctx;
    }

    @Override
    public ByteBuf run( Client client ){
        HardwareClientsInfo.INSTANCE.writeResponse( client.getClientId(), result );
        return null;
    }


    @Override
    void parse(){
//
        result = data.readInt();
    }

}
