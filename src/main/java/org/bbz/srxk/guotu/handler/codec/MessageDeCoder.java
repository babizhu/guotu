package org.bbz.srxk.guotu.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liulaoye on 17-2-20.
 * 根据包长度来解包
 */
public class MessageDeCoder extends LengthFieldBasedFrameDecoder{
    private static final Logger LOG = LoggerFactory.getLogger( MessageDeCoder.class );

    private final static int MAX_FRAME_LENGTH = 8192,
            LENGTH_FILED_OFFSET = 1,
            LENGTH_ADJUSTMENT = -1,
            LENGTH_FIELD_LENGTH = 1;//真实环境
//            LENGTH_FIELD_LENGTH = 2;//大包压力测试环境

    public MessageDeCoder( int maxFrameLength, int lengthFieldOffset, int lengthFieldLength ){
        super( maxFrameLength, lengthFieldOffset, lengthFieldLength,LENGTH_ADJUSTMENT,0 );
    }

    public MessageDeCoder(){
        this( MAX_FRAME_LENGTH, LENGTH_FILED_OFFSET, LENGTH_FIELD_LENGTH );
    }

    @Override
    protected Object decode( ChannelHandlerContext ctx, ByteBuf in ) throws Exception{
        final ByteBuf frame = (ByteBuf) super.decode( ctx, in );
        if( frame == null ) {
            return null;
        }
        byte head = frame.readByte();
        short len = frame.readUnsignedByte();//真实环境
//        short len = frame.readShort();//测试环境
        short cmdId = frame.readUnsignedByte();
        int dataLen = frame.writerIndex() - 2 - 7 - frame.readerIndex();//2 for checksum, 7 from tiemstamp
        ByteBuf data = frame.slice(frame.readerIndex(),dataLen );
        frame.skipBytes( dataLen + 7 );//7 from tiemstamp
        byte[] checkSum = new byte[2];//2 for checksum
        frame.readBytes( checkSum );

        return new MessageContainer( head, len, cmdId, data, checkSum );
//        return new String(frame.getInt( 2 ) + "");
//        return frame;
    }
}
