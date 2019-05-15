/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2019-05-15
 */
package com.thebeastshop.tx.dubbo.springboot;

import com.thebeastshop.tx.aop.TxAspect;
import com.thebeastshop.tx.dubbo.spring.DubboMethodScanner;
import com.thebeastshop.tx.socket.client.SocketClient;
import com.thebeastshop.tx.socket.factory.SocketClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * tx dubbo的自动装配
 */
@Configuration
public class TxDubboAutoConfiguation {

    @Value("${tx.monitor.ip}")
    private String txMonitorIp;

    @Value("${tx.monitor.port}")
    private int txMonitorPort;

    @Bean
    @ConditionalOnProperty(prefix = "tx.monitor",name = "ip")
    public SocketClientFactory socketClientFactory(){
        SocketClientFactory socketClientFactory = new SocketClientFactory();
        socketClientFactory.setIp(txMonitorIp);
        socketClientFactory.setPort(txMonitorPort);
        return socketClientFactory;
    }

    @Bean
    public TxAspect txAspect(){
        return new TxAspect();
    }

    @Bean
    public DubboMethodScanner dubboMethodScanner(){
        return new DubboMethodScanner();
    }

    public String getTxMonitorIp() {
        return txMonitorIp;
    }

    public void setTxMonitorIp(String txMonitorIp) {
        this.txMonitorIp = txMonitorIp;
    }

    public int getTxMonitorPort() {
        return txMonitorPort;
    }

    public void setTxMonitorPort(int txMonitorPort) {
        this.txMonitorPort = txMonitorPort;
    }
}
