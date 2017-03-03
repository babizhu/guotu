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
    private Map<Integer,Client> clients = new ConcurrentHashMap<>();


    public void add( int clientId, ChannelHandlerContext ctx ){
        final Client client = new Client( clientId, ctx );
        clients.put( clientId,client );
    }

    public Client getClient( int clientId ){
        return clients.get( clientId );
    }

    /**
     *
     * @param clientId
     * @return
     */
    public Client remove( int clientId ){
        final Client removeClient = clients.remove( clientId );
        if( removeClient != null ){
            //TODO 额外的操作
        }
        removeClient.getCtx().close();
//        ctx.channel().close();
        return removeClient;
    }

}
