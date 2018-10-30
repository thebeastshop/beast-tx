package com.thebeastshop.tx.test.sc.provider.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @RequestMapping("/hi")
    public String sayHello(@RequestParam String name){
        System.out.println("invoke method hi,name="+name);
        String services = "Services:" + discoveryClient.getServices();
        System.out.println(services);
        return "hello," + name;
    }
}
