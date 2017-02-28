package org.bbz.srxk.guotu.client;

import io.netty.channel.ChannelHandlerContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liulaoye on 17-2-27.
 * online clients 管理
 */
public enum ClientsInfo{
    INSTANCE;
    private Map<ChannelHandlerContext,Client> clients = new ConcurrentHashMap<>();


    public void add( int clientId, ChannelHandlerContext ctx ){
        final Client client = new Client( clientId, ctx );
        clients.put( ctx,client );
    }

    public Client getClient( ChannelHandlerContext ctx ){
        return clients.get( ctx );
    }
    public Client remove( ChannelHandlerContext ctx ){
        final Client removeClient = clients.remove( ctx );
        ctx.close();
//        ctx.channel().close();
        return removeClient;
    }
}
