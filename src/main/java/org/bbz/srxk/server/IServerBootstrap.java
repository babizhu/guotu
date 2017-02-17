package org.bbz.srxk.server;

import java.net.InetSocketAddress;

/**
 * Created by liulaoye on 17-2-17.
 */
public interface IServerBootstrap{
    /**
     * <p>
     * Give the server a name (used for naming threads, useful for logging).
     * </p>
     * <p>
     * <p>
     * Default = LittleProxy
     * </p>
     *
     * @param name
     * @return
     */
    IServerBootstrap withName( String name );


    /**
     * <p>
     * Listen for incoming connections on the given address.
     * </p>
     * <p>
     * <p>
     * Default = [bound ip]:8080
     * </p>
     *
     * @param address
     * @return
     */
    IServerBootstrap withAddress( InetSocketAddress address );

    /**
     * <p>
     * Listen for incoming connections on the given port.
     * </p>
     * <p>
     * <p>
     * Default = 8080
     * </p>
     *
     * @param port
     * @return
     */
    IServerBootstrap withPort( int port );

    /**
     * <p>
     * Specify whether or not to only allow local connections.
     * </p>
     * <p>
     * <p>
     * Default = true
     * </p>
     *
     * @param allowLocalOnly
     * @return
     */
    IServerBootstrap withAllowLocalOnly( boolean allowLocalOnly );


    /**
     * <p>
     * Specify the timeout after which to disconnect idle connections, in
     * seconds.
     * </p>
     * <p>
     * <p>
     * Default = 70
     * </p>
     *
     * @param idleConnectionTimeout
     * @return
     */
    IServerBootstrap withIdleConnectionTimeout( int idleConnectionTimeout );



    IServerBootstrap withMaxFrameSize( int maxChunkSize );

    /**
     * Sets the alias to use when adding Via headers to incoming and outgoing HTTP messages. The alias may be any
     * pseudonym, or if not specified, defaults to the hostname of the local machine. See RFC 7230, section 5.7.1.
     *
     * @param alias the pseudonym to add to Via headers
     */
    IServerBootstrap withServerAlias( String alias );

    /**
     * <p>
     * Build and starts the server.
     * </p>
     *
     * @return the newly built and started server
     */
    IServer start();
}
