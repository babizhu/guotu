package org.bbz.srxk.guotu.handler.cmd;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GongdiSendCmd{
    private static final Logger LOG = LoggerFactory.getLogger( GongdiSendCmd.class );
    private final ByteBuf data;
    private final ChannelHandlerContext ctx;

    private byte[] baseStationId = new byte[5];
    private byte[] keyId = new byte[12];

    public int getCmdId(){
        return cmdId;
    }

    private short cmdId;

    public GongdiSendCmd( ChannelHandlerContext ctx, ByteBuf data ){
        this.data = data;
        this.ctx = ctx;
        parse();
    }

    public String buildExitKey(){
        String keyIdStr = ByteBufUtil.hexDump( keyId );
        String baseStationStr = ByteBufUtil.hexDump( baseStationId );
        return baseStationStr + keyIdStr + GongdiCmd.EXIT.toNum();
    }

    public String buildAlertUrl(){
        String url = "host?";
        String keyIdStr = ByteBufUtil.hexDump( keyId );
        String baseStationStr = ByteBufUtil.hexDump( baseStationId );

        url += "keyId=" + keyIdStr;
        url += "&&baseStationId=" + baseStationStr;
        url += "&&cmdId=" + GongdiCmd.ALERT.toNum();
        return url;
    }

    public String buildUrl(){
        String url = "host?";
        String keyIdStr = ByteBufUtil.hexDump( keyId );
        String baseStationStr = ByteBufUtil.hexDump( baseStationId );
        String cmdIdStr = this.cmdId + "";
        url += "keyId=" + keyIdStr;
        url += "&&baseStationId=" + baseStationStr;
        url += "&&cmdId=" + cmdIdStr;
        return url;
    }

    public ByteBuf run(){
        String url = buildUrl();
//        System.out.println( "send cmd url=" + url );
        sendRequest( url );
        return null;
    }

    private void parse(){
        ByteBufUtil.hexDump( data );
        data.readBytes( baseStationId );
        cmdId = data.readUnsignedByte();
        data.readBytes( keyId );
//        LOG.debug( toString() );
    }


    public void sendAlertCmd(){
        String url = buildAlertUrl();
//        System.out.println( "url=" + url );
        sendRequest( url );
    }

    private void sendRequest( String url ){
        System.out.println( "GongdiSendCmd.sendRequest:" + url );
    }

}