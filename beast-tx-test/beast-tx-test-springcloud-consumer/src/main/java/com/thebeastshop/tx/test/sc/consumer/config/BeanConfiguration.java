package com.thebeastshop.tx.test.sc.consumer.config;

import com.thebeastshop.tx.aop.TxAspect;
import com.thebeastshop.tx.feign.aop.FeignTxAspect;
import com.thebeastshop.tx.feign.spring.FeignMethodScanner;
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
}
