package com.thebeastshop.tx.test.sc.consumer.config;

import com.thebeastshop.tx.feign.spring.FeignMethodScanner;
import feign.Request;
import feign.Retryer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    public static int connectTimeOutMillis = 8000;
    public static int readTimeOutMillis = 8000;

    @Bean
    public Request.Options options() {
        return new Request.Options(connectTimeOutMillis, readTimeOutMillis);
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
}
