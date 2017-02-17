package org.bbz.srxk.server;

import java.net.InetSocketAddress;
import java.util.Properties;

/**
 * Created by liulaoye on 17-2-17.
 */
public class ServerBootstrap implements IServerBootstrap{
    public ServerBootstrap( Properties props ){


        this.withPort( Integer.parseInt( props.getProperty( "port", DefaultServer.PORT + "" ) ) );
    }

    @Override
    public IServerBootstrap withName( String name ){
        return null;
    }

    @Override
    public IServerBootstrap withAddress( InetSocketAddress address ){
        return null;
    }

    @Override
    public IServerBootstrap withPort( int port ){
        return null;
    }

    @Override
    public IServerBootstrap withAllowLocalOnly( boolean allowLocalOnly ){
        return null;
    }

    @Override
    public IServerBootstrap withIdleConnectionTimeout( int idleConnectionTimeout ){
        return null;
    }

    @Override
    public IServerBootstrap withMaxFrameSize( int maxChunkSize ){
        return null;
    }

    @Override
    public IServerBootstrap withServerAlias( String alias ){
        return null;
    }

    @Override
    public IServer start(){
        return null;
    }

}
