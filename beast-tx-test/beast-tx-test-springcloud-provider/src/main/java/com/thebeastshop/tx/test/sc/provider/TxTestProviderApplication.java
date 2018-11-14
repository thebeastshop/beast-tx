package com.thebeastshop.tx.test.sc.provider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class TxTestProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(TxTestProviderApplication.class, args);
    }
}
