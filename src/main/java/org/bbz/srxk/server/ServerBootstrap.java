package org.bbz.srxk.server;

import org.bbz.srxk.misc.PropertiesUtil;

import java.net.InetSocketAddress;
import java.util.Properties;

/**
 * Created by liulaoye on 17-2-17.
 * 启动类
 */
public class ServerBootstrap implements IServerBootstrap {

    /**
     * server 监听端口
     */
    private int port;
    private int maxFrameSize;
    private InetSocketAddress requestedAddress;
    private boolean allowLocalOnly;

    public ServerBootstrap(Properties props) {


        this.withPort(PropertiesUtil.getInt(props, "port", DefaultServer.PORT_DEFAULT));
    }


    @Override
    public IServerBootstrap withName(String name) {
        return null;
    }

    @Override
    public IServerBootstrap withAddress(InetSocketAddress address) {
        return null;
    }

    @Override
    public IServerBootstrap withPort(int port) {
        this.port = port;
        return this;
    }

    @Override
    public IServerBootstrap withAllowLocalOnly(boolean allowLocalOnly) {
        return null;
    }

    @Override
    public IServerBootstrap withIdleConnectionTimeout(int idleConnectionTimeout) {
        return null;
    }

    @Override
    public IServerBootstrap withMaxFrameSize(int maxChunkSize) {
        this.maxFrameSize = maxChunkSize;
        return this;
    }

    @Override
    public IServerBootstrap withServerAlias(String alias) {
        return null;
    }

    @Override
    public IServer start() {
        return build().start();
    }

    private DefaultServer build() {
        return new DefaultServer(determineListenAddress());
    }

    private InetSocketAddress determineListenAddress() {
        if (requestedAddress != null) {
            return requestedAddress;
        } else {
            // Binding only to localhost can significantly improve the
            // security of the proxy.
            if (allowLocalOnly) {
                return new InetSocketAddress("127.0.0.1", port);
            } else {
                return new InetSocketAddress(port);
            }
        }
    }

}
