package org.bbz.srxk.guotu.handler.cmd;

import io.netty.buffer.ByteBuf;

/**
 * Created by liukun on 2017/2/25.
 */
public abstract class AbstractCmd {
    final ByteBuf data;
    abstract void parse();

    protected AbstractCmd(ByteBuf data) {
        this.data = data;
        parse();
    }

    abstract void run();
}
