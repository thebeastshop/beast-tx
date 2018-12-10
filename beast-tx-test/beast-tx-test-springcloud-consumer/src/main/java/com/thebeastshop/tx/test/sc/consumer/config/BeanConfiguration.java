package com.thebeastshop.tx.test.sc.consumer.config;

import com.thebeastshop.tx.aop.TxAspect;
import com.thebeastshop.tx.feign.aop.FeignTxAspect;
import com.thebeastshop.tx.feign.spring.FeignMethodScanner;
import com.thebeastshop.tx.socket.client.SocketClient;
import com.thebeastshop.tx.socket.factory.SocketClientFactory;
import feign.Request;
import feign.Retryer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/config.properties")
public class BeanConfiguration {

    @Value("${http.connectTimeOutMillis}")
    private int connectTimeOutMillis;

    @Value("${http.readTimeOutMillis}")
    private int readTimeOutMillis;

    @Bean
    public Request.Options options() {
        return new Request.Options(connectTimeOutMillis,readTimeOutMillis);
    }

    @Bean
    public Retryer feignRetryer() {
        return Retryer.NEVER_RETRY;
    }

    @Bean
    public FeignMethodScanner feignMethodScanner(){
        FeignMethodScanner scanner = new FeignMethodScanner();
        scanner.setFeignPackage("com.thebeastshop.tx.test.sc.consumer.feign");
        return scanner;
    }

    @Bean
    public TxAspect txAspect(){
        return new TxAspect();
    }

    @Bean
    public FeignTxAspect feignTxAspect(){
        return new FeignTxAspect();
    }

    @Bean
    public SocketClientFactory socketClient() throws Exception {
        SocketClientFactory factory=  new SocketClientFactory();
        factory.setIp("127.0.0.1");
        factory.setPort(6789);
        return factory;
    }

}
