package org.bbz.srxk.guotu.handler.codec;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.bbz.srxk.guotu.handler.cmd.Cmd;

import java.util.Calendar;
import java.util.Date;

import static io.netty.buffer.ByteBufUtil.appendPrettyHexDump;
import static io.netty.util.internal.StringUtil.NEWLINE;

/**
 * Created by liulaoye on 17-3-7.
 * 一个用于测试服务器的client类
 */
public class BaseClient{

    private static final byte HEAD = (byte) 0XFF;

    public void connect( String host, int port, ChannelInboundHandlerAdapter handlerAdapter ){

        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group( workerGroup ); // (2)
            b.channel( NioSocketChannel.class ); // (3)
            b.option( ChannelOption.SO_KEEPALIVE, true ); // (4)
            b.option( ChannelOption.CONNECT_TIMEOUT_MILLIS, 1000);
            b.handler( new ChannelInitializer<SocketChannel>(){
                @Override
                public void initChannel( SocketChannel ch ) throws Exception{
                    ch.pipeline().addLast( handlerAdapter );
                }
            } );

            ChannelFuture f = b.connect( host, port ).sync();
            f.channel().closeFuture().sync();
        } catch( InterruptedException e ) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }


    /**
     * 格式化客户端反馈的信息，方便查看
     *
     * @param ctx
     * @param eventName
     * @param msg
     * @return
     */
    public String formatByteBuf( ChannelHandlerContext ctx, String eventName, ByteBuf msg ){
        String chStr = ctx.channel().toString();
        int length = msg.readableBytes();
        if( length == 0 ) {
            StringBuilder buf = new StringBuilder( chStr.length() + 1 + eventName.length() + 4 );
            buf.append( chStr ).append( ' ' ).append( eventName ).append( ": 0B" );
            return buf.toString();
        } else {
            int rows = length / 16 + (length % 15 == 0 ? 0 : 1) + 4;
            StringBuilder buf = new StringBuilder( chStr.length() + 1 + eventName.length() + 2 + 10 + 1 + 2 + rows * 80 );

            buf.append( chStr ).append( ' ' ).append( eventName ).append( ": " ).append( length ).append( 'B' ).append( NEWLINE );
            appendPrettyHexDump( buf, msg );

            return buf.toString();
        }
    }

    /**
     * 构建各种命令包
     *
     * @param buffer
     * @param cmd
     * @return
     */
    ByteBuf buildCmd( ByteBuf buffer, Cmd cmd ){
        buffer.writeByte( HEAD );
        buffer.writeByte( 0 );//长度
        if( cmd == Cmd.UNDEFINED_CMD ) {
            buffer.writeByte( 244 );//故意发送一个错误的cmdId

        } else {
            buffer.writeByte( (byte) cmd.toNum() );
        }
        switch( cmd ) {
            case RAINFALL_CMD:
                buildRainfallCmd( buffer );
                break;
            case LOGIN_CMD:
                buildLoginCmd( buffer );
                break;
            case CTRL_CMD:
                buildCtrlCmd( buffer );
                break;
            default:
                System.out.println( "未找到的命令:" + cmd );
        }

        byte[] checkSum = new byte[2];
        buffer.writeBytes( checkSum );

        buffer.setByte( 1, buffer.writerIndex() - 1 );// 1 for head
        return buffer;
    }

    private void buildCtrlCmd( ByteBuf buffer ){
        buffer.writeInt( -111 );//模拟客户端相应服务器的控制指令，1成功，0失败
    }

    void buildRainfallCmd( ByteBuf buffer ){
        buffer.writeByte( 0x03 );
        buffer.writeByte( 0x04 );
        buffer.writeByte( 0x00 );
        buffer.writeByte( 0x00 );
        buffer.writeShort( 1334 );
        buffer.writeByte( 0xfa );
        buffer.writeByte( 0x33 );

        /**********************时间戳*************************/
        Calendar cal = Calendar.getInstance();
        cal.setTime( new Date() );
        buffer.writeByte( cal.get( Calendar.HOUR_OF_DAY ) );//时
        buffer.writeByte( cal.get( Calendar.MINUTE ) );//分
        buffer.writeByte( cal.get( Calendar.SECOND ) );//秒

        buffer.writeByte( cal.get( Calendar.YEAR ) % 2000 );//年
        buffer.writeByte( cal.get( Calendar.MONTH ) + 1 );//月
        buffer.writeByte( cal.get( Calendar.DAY_OF_MONTH ) );//日
        int dayOfWeek = cal.get( Calendar.DAY_OF_WEEK );
        dayOfWeek = dayOfWeek == 1 ? 7 : dayOfWeek - 1;
        buffer.writeByte( dayOfWeek );//星期
        /**********************时间戳*************************/

    }

    void buildLoginCmd( ByteBuf buffer ){
        byte byte1 = (byte) 0x88;
        byte byte2 = (byte) 0x99;
        buffer.writeByte( byte1 );
        buffer.writeByte( byte2 );
        buffer.writeInt( 19 );//clientID
        buffer.writeByte( byte2 );
        buffer.writeByte( byte1 );

    }
}
