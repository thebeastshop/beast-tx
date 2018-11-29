package com.thebeastshop.tx.test.sc.provider.controller;

import com.thebeastshop.tx.test.sc.provider.service.domain.ProviderDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private ProviderDomain providerDomain;

    @RequestMapping("/hi")
    public String sayHello(@RequestParam String name){
        System.out.println("invoke method hi,name="+name);
        String services = "Services:" + discoveryClient.getServices();
        System.out.println(services);
        return "hello," + name;
    }

    @RequestMapping("/tryTest1")
    public boolean tryTest1(@RequestParam String name){
        log.info("invoked tryTest1");
        return providerDomain.tryTest1(name);
    }

    @RequestMapping("/test1")
    public String test1(@RequestParam String name){
        log.info("invoked test1");
        return providerDomain.test1(name);
    }

    @RequestMapping("/cancelTest1")
    public void cancelTest1(@RequestParam String name){
        log.info("invoked cancelTest1");
        providerDomain.cancelTest1(name);
    }

    public String test2(@RequestParam String name){
        return providerDomain.test2(name);
    }
}
