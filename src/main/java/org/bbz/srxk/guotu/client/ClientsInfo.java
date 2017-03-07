package org.bbz.srxk.guotu.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by liulaoye on 17-2-27.
 * online clients 管理
 */
public enum ClientsInfo{
    INSTANCE;
    private Map<Integer, Client> clients = new ConcurrentHashMap<>();


    public void add( int clientId, ChannelHandlerContext ctx ){
        final Client client = new Client( clientId, ctx );
        clients.put( clientId, client );
    }

    public Client getClient( int clientId ){
        return clients.get( clientId );
    }

    /**
     * @param clientId
     * @return
     */
    public Client remove( int clientId ){
        final Client removeClient = clients.remove( clientId );
        if( removeClient != null ) {
            if( responseMap.containsKey( removeClient.getCtx() ) ) {
                responseMap.remove( removeClient.getCtx() );
            }
        }
        removeClient.getCtx().close();
        return removeClient;
    }


    private Map<Integer, Queue<Object>> responseMap = new HashMap<>();//Integer is clientId

    //    public void waitResult(String ctx){
//        final Queue<Object> queue = responseMap.get( ctx );
//
//
//    }
//
    synchronized
    public void writeResponse( int clientId, Object result ){
        final Queue<Object> queue = responseMap.get( clientId );
        if( queue != null ) {
            queue.add( result );
        }
    }

    /**
     * 向硬件客户端发送控制指令
     * @param clientId      客户端id
     * @param args          命令参数
     * @return              阻塞队列
     */
    synchronized
    public Queue<Object> sendCtrlCmd( int clientId, String args ){

        final Client client = this.getClient( clientId );
        if( client == null || client.getCtx() == null ) {
            return null;
        }
        final ChannelHandlerContext ctx = client.getCtx();

        doSendCtrlCmd(  ctx,args );
        if( responseMap.containsKey( clientId ) ) {
            return responseMap.get( clientId );
        } else {
            Queue<Object> queue = new ArrayBlockingQueue<>( 10 );
            responseMap.put( clientId, queue );
            return queue;
        }
    }

    private void doSendCtrlCmd( ChannelHandlerContext ctx,  String args ){
        final ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeInt( Integer.parseInt( args ) );
        ctx.writeAndFlush( buffer );
    }

}
