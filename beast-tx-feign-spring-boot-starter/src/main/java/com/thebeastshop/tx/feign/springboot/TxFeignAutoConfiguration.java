/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2019/5/16
 */
package com.thebeastshop.tx.feign.springboot;

import com.thebeastshop.tx.aop.TxAspect;
import com.thebeastshop.tx.feign.aop.FeignTxAspect;
import com.thebeastshop.tx.feign.spring.FeignMethodScanner;
import com.thebeastshop.tx.socket.client.SocketClient;
import com.thebeastshop.tx.socket.factory.SocketClientFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.lang.Nullable;

@Configuration
public class TxFeignAutoConfiguration {

    private final String TX_MONITOR_IP = "tx.monitor.ip";

    private final String TX_MONITOR_PORT = "tx.monitor.port";

    @Bean
    @ConditionalOnProperty(prefix = "tx.monitor",name = {"ip","port"})
    public SocketClientFactory socketClientFactory(Environment environment){
        SocketClientFactory socketClientFactory = new SocketClientFactory();
        socketClientFactory.setIp(environment.getProperty(TX_MONITOR_IP));
        socketClientFactory.setPort(environment.getProperty(TX_MONITOR_PORT,Integer.class));
        return socketClientFactory;
    }

    @Bean
    public TxAspect txAspect(@Nullable SocketClient socketClient){
        return new TxAspect();
    }

    @Bean
    public FeignTxAspect feignTxAspect(){
        return new FeignTxAspect();
    }

    @Bean
    public FeignMethodScanner feignMethodScanner(){
        return new FeignMethodScanner();
    }
}
