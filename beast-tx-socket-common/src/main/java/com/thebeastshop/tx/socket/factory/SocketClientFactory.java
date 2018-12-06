/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/12/5
 */
package com.thebeastshop.tx.socket.factory;

import com.thebeastshop.tx.socket.client.SocketClient;
import com.thebeastshop.tx.socket.client.SocketClientHandler;
import com.thebeastshop.tx.socket.client.SocketClientProvider;
import com.thebeastshop.tx.socket.config.ClientConfig;
import org.springframework.beans.factory.FactoryBean;

public class SocketClientFactory implements FactoryBean<SocketClient> {

    private String ip;

    private int port;

    private SocketClientHandler handler;

    @Override
    public SocketClient getObject() throws Exception {
        ClientConfig config = new ClientConfig();
        config.setIp(ip);
        config.setPort(port);
        config.setHandler(handler);
        SocketClient client = SocketClientProvider.create(config);
        return client;
    }

    @Override
    public Class<?> getObjectType() {
        return SocketClient.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public SocketClientHandler getHandler() {
        return handler;
    }

    public void setHandler(SocketClientHandler handler) {
        this.handler = handler;
    }
}
