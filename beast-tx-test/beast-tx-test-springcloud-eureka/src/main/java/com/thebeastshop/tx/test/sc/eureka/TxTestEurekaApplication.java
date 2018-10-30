package com.thebeastshop.tx.test.sc.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class TxTestEurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(TxTestEurekaApplication.class, args);
    }
}
