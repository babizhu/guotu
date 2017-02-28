package org.bbz.srxk.guotu.client;

import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

/**
 * Created by liulaoye on 17-2-27.
 *
 */

@Data
public class Client{
    private final int clientId;
    private final ChannelHandlerContext ctx;

    Client( int clientId, ChannelHandlerContext ctx ){
        this.clientId = clientId;
        this.ctx = ctx;
    }


}
