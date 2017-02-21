package org.bbz.srxk.guotu.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
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
            LENGTH_FIELD_LENGTH = 1;

    public MessageDeCoder( int maxFrameLength, int lengthFieldOffset, int lengthFieldLength ){
        super( maxFrameLength, lengthFieldOffset, lengthFieldLength );
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
        LOG.debug( ByteBufUtil.hexDump( frame ) );
        return new String(frame.getInt( 2 ) + "");
//        return frame;
    }
}
