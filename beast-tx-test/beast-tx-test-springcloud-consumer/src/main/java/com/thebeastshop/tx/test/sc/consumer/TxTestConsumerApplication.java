package com.thebeastshop.tx.test.sc.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
public class TxTestConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TxTestConsumerApplication.class, args);
    }
}
