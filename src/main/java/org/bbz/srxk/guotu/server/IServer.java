package org.bbz.srxk.guotu.server;

import java.net.InetSocketAddress;

/**
 * Created by liulaoye on 17-2-17.
 */
public interface IServer{


    int getIdleConnectionTimeout();

    void setIdleConnectionTimeout( int idleConnectionTimeout );

    /**
     * Returns the maximum time to wait, in milliseconds, to connect to a server.
     */
    int getConnectTimeout();

    /**
     * Sets the maximum time to wait, in milliseconds, to connect to a server.
     */
    void setConnectTimeout( int connectTimeoutMs );


    /**
     * Stops the server and all related clones. Waits for traffic to stop before shutting down.
     */
    void stop();

    /**
     * Stops the server and all related clones immediately, without waiting for traffic to stop.
     */
    void abort();

    /**
     * Return the address on which this proxy is listening.
     *
     * @return
     */
    InetSocketAddress getListenAddress();

    /**
     * <p>
     * Set the read/write throttle bandwidths (in bytes/second) for this proxy.
     * </p>
     *
     * @param readThrottleBytesPerSecond
     * @param writeThrottleBytesPerSecond
     */
    void setThrottle( long readThrottleBytesPerSecond, long writeThrottleBytesPerSecond );

}


