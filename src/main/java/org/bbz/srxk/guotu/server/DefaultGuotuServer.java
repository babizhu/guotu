package org.bbz.srxk.guotu.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.bbz.srxk.guotu.handler.ChannelInitializerHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by liulaoye on 17-2-17.
 * 缺省的服务器实现
 */
public class DefaultGuotuServer implements IServer{

    private static final Logger LOG = LoggerFactory.getLogger( DefaultGuotuServer.class );

    /**
     * 缺省的服务器名字
     */

    private static final String SERVER_ALIAS_DEFAULT = "GUOTU server";

    /**
     * 最大的包长度
     */
    private static final int MAX_FRAME_SIZE_DEFAULT = 2048;

    /**
     * 缺省的监听端口
     */
    public static final int PORT_DEFAULT = 8080;

    /**
     * The alias or pseudonym for this server, used when adding the Via header.
     */
    private final String proxyAlias = "guotu transform server";


    /**
     * True when the server has already been stopped by calling {@link #stop()} or {@link #abort()}.
     */
    private final AtomicBoolean stopped = new AtomicBoolean( false );


    /**
     * Keep track of all channels created by this  server for later shutdown when the proxy is stopped.
     */
    private final ChannelGroup allChannels = new DefaultChannelGroup( proxyAlias, GlobalEventExecutor.INSTANCE );
    private final Thread jvmShutdownHook = new Thread( new Runnable(){

        @Override
        public void run(){
            abort();
        }
    }, "GUOTO-SERVER-JVM-shutdown-hook" );

//    private final int maxFramesize;
//    private final int lengthFieldOffset;
    private InetSocketAddress requestedAddress, listenAddress;

    public DefaultGuotuServer( InetSocketAddress requestedAddress){
        this.requestedAddress = requestedAddress;
//        this.maxFramesize = maxFrameSize;
//        lengthFieldOffset = 1;
    }


    public int getIdleConnectionTimeout(){
        return 0;
    }

    public void setIdleConnectionTimeout( int idleConnectionTimeout ){

    }

    public int getConnectTimeout(){
        return 0;
    }

    public void setConnectTimeout( int connectTimeoutMs ){

    }

    public void stop(){

    }

    public void abort(){

    }

    public InetSocketAddress getListenAddress(){
        return listenAddress;
    }

    public void setThrottle( long readThrottleBytesPerSecond, long writeThrottleBytesPerSecond ){

    }

//    public int getMaxFrameSize(){
//        return maxFramesize;
//    }

    public static IServerBootstrap bootstrapFromFile( String path ){
        final File propsFile = new File( path );
        Properties props = new Properties();

        if( propsFile.isFile() ) {
            try( InputStream is = new FileInputStream( propsFile ) ) {
                props.load( is );
            } catch( final IOException e ) {
                LOG.warn( "Could not load props file?", e );
            }
        }

        return new GuotuServerBootstrap( props );
    }

    protected IServer start(){

        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();

        final ServerBootstrap b = new ServerBootstrap();
        b.group( boss, worker )
                .channel( NioServerSocketChannel.class )
//                .handler(new LoggingHandler( LogLevel.INFO))
                .childHandler( new ChannelInitializerHandler( this ) )
                .option( ChannelOption.SO_BACKLOG, 128 );

        ChannelFuture future = b.bind( requestedAddress )
                .addListener( new ChannelFutureListener(){
                    @Override
                    public void operationComplete( ChannelFuture future ) throws Exception{
                        if( future.isSuccess() ) {
                            registerChannel( future.channel() );
                        }
                    }
                } ).awaitUninterruptibly();

        Throwable cause = future.cause();
        if( cause != null ) {
            throw new RuntimeException( cause );
        }

        this.listenAddress = ((InetSocketAddress) future.channel().localAddress());


        Runtime.getRuntime().addShutdownHook( jvmShutdownHook );
        return this;
    }

    private void registerChannel( Channel channel ){
    }


}
