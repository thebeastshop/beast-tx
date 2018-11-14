package com.thebeastshop.tx.test.sc.consumer.controller;

import com.thebeastshop.tx.test.sc.consumer.domain.ConsumerDomain;
import com.thebeastshop.tx.test.sc.consumer.feign.TxTestSayhiFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TxTestConsumerController {

    @Autowired
    private ConsumerDomain consumerDomain;

    @RequestMapping("/hi")
    public String sayHello(@RequestParam String name){
        return consumerDomain.doTest(name);
    }
}
