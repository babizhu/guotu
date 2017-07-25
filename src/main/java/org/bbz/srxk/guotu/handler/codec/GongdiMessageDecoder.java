package org.bbz.srxk.guotu.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * Created by liulaoye on 17-2-20.
 * 根据包长度来解包
 */
public class GongdiMessageDecoder extends LengthFieldBasedFrameDecoder{
//    private static final Logger LOG = LoggerFactory.getLogger( MessageDecoder.class );

    private final static int MAX_FRAME_LENGTH = 1024,
            LENGTH_FILED_OFFSET = 1,
            LENGTH_ADJUSTMENT = -1,
            LENGTH_FIELD_LENGTH = 1;//真实环境
//            LENGTH_FIELD_LENGTH = 2;//大包压力测试环境

    public GongdiMessageDecoder( int maxFrameLength, int lengthFieldOffset, int lengthFieldLength ){
        super( maxFrameLength, lengthFieldOffset, lengthFieldLength, LENGTH_ADJUSTMENT, 0 );
    }

    public GongdiMessageDecoder(){
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
        short cmdId = frame.getUnsignedByte( 7 );
        int dataLen = frame.writerIndex()  - frame.readerIndex();//2 for checksum
        ByteBuf data = frame.slice( frame.readerIndex(), dataLen );


        return new GongdiMessageContainer( head, len, cmdId, data);
//        return new String(frame.getInt( 2 ) + "");
//        return frame;
    }


}
