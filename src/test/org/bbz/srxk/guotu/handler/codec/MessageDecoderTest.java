package org.bbz.srxk.guotu.handler.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by liulaoye on 17-2-20.
 * test
 */
public class MessageDecoderTest{

    @Test
    public void testFramesDecoded(){
        int cmdId = 109;
        ByteBuf buf = Unpooled.buffer();
        buf.writeByte( 0xff );//head
        buf.writeByte( 10 );//length
        buf.writeByte( cmdId );//cmdId
        for( int i = 0; i < 8; i++ ) {
            buf.writeByte( i );
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel( new MessageDecoder() );


        Assert.assertTrue( channel.writeInbound( input ) );
        Assert.assertTrue( channel.finish() );

        final MessageContainer o = channel.readInbound();
        System.out.println( o );
        Assert.assertEquals( cmdId, o.getCmdId() );

        Assert.assertNull( channel.readInbound() );

    }

    @Test
    public void testFramesDecoded2(){
        ByteBuf buf = Unpooled.buffer();
        for( int i = 0; i < 9; i++ ) {
            buf.writeByte( i );
        }
        ByteBuf input = buf.duplicate();
        EmbeddedChannel channel = new EmbeddedChannel( new MessageDecoder() );
        Assert.assertFalse( channel.writeInbound( input.readBytes( 2 ) ) );
        Assert.assertTrue( channel.writeInbound( input.readBytes( 7 ) ) );
        Assert.assertTrue( channel.finish() );

        Assert.assertEquals( buf.readBytes( 3 ), channel.readInbound() );
        Assert.assertEquals( buf.readBytes( 3 ), channel.readInbound() );
        Assert.assertEquals( buf.readBytes( 3 ), channel.readInbound() );
        Assert.assertNull( channel.readInbound() );
    }

}