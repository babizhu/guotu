package org.bbz.srxk.guotu.handler.cmd;

import io.netty.buffer.ByteBuf;
import org.bbz.srxk.guotu.client.Client;

/**
 * Created by liukun on 2017/2/25.
 *
 */
abstract class AbstractCmd{
    final ByteBuf data;


    abstract void parse();

    AbstractCmd( ByteBuf data ){
        this.data = data;
        parse();
    }

    abstract ByteBuf run( Client client );
}
