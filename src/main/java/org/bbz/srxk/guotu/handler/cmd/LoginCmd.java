package org.bbz.srxk.guotu.handler.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.bbz.srxk.guotu.client.Client;
import org.bbz.srxk.guotu.client.HardwareClientsInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by liukun on 2017/2/25.
 * logincmd
 */
public class LoginCmd extends AbstractCmd{

    private static final Logger LOG = LoggerFactory.getLogger( LoginCmd.class );
    public static final AttributeKey<Integer> ATTR_ID_KEY = AttributeKey.valueOf( "clentId" );


    private final ChannelHandlerContext ctx;

    private int clientId;

    public LoginCmd( ChannelHandlerContext ctx, ByteBuf data ){
        super( data );
        this.ctx = ctx;
    }

    @Override
    public ByteBuf run( Client client ){

        if( ctx.channel().hasAttr( ATTR_ID_KEY ) ) {
            LOG.error( "已登录用户重复登录:" +  ctx.channel().attr( ATTR_ID_KEY ) + ", ip 为" + ctx.channel().remoteAddress());

        } else {

            ctx.channel().attr( ATTR_ID_KEY ).set( clientId );
        }
        HardwareClientsInfo.INSTANCE.add( clientId, ctx );
        return buildServerTime() ;
    }

    private ByteBuf buildServerTime(){
//        F0 BB 78 52 0E 1E 23 0E 07 0B 07 23 E8
//        F0 BB 78 52时分秒年月日星期23 E8   

        ByteBuf timeBuf = ctx.alloc().buffer();
        timeBuf.writeByte( 0xf0 );
        timeBuf.writeByte( 0xbb );
        timeBuf.writeByte( 0x78 );
        timeBuf.writeByte( 0x52 );

        Calendar cal = Calendar.getInstance();
        cal.setTime( new Date() );

        timeBuf.writeByte( cal.get( Calendar.HOUR_OF_DAY ) );//时
        timeBuf.writeByte( cal.get( Calendar.MINUTE ) );//分
        timeBuf.writeByte( cal.get( Calendar.SECOND ) );//秒

        timeBuf.writeByte( cal.get( Calendar.YEAR ) - 2000 );//年
        timeBuf.writeByte( cal.get( Calendar.MONTH ) + 1 );//月
        timeBuf.writeByte( cal.get( Calendar.DAY_OF_MONTH ) );//日
        int dayOfWeek = cal.get( Calendar.DAY_OF_WEEK );
        dayOfWeek = dayOfWeek == 1 ? 7 : dayOfWeek - 1;
        timeBuf.writeByte( dayOfWeek );//星期


        timeBuf.writeByte( 0x23 );
        timeBuf.writeByte( 0xe8 );

        return timeBuf;
    }

    @Override
    public String toString(){
        return "LoginCmd{" +
                "clientId=" + clientId +
                '}';
    }

    @Override
    void parse(){
        clientId = data.getInt( 2 );
        LOG.debug( toString() );
    }
}
