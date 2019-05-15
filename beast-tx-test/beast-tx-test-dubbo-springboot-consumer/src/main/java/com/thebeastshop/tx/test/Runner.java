package com.thebeastshop.tx.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource(locations = {"classpath:/applicationContext-dubbo.xml"})
public class Runner {

    public static void main(String[] args) throws Throwable {
        try {
            SpringApplication.run(Runner.class, args);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        while (true) {
            Thread.sleep(60000);
        }
    }
}
