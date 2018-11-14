package com.thebeastshop.tx.test.sc.consumer.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("provider")
public interface TxTestSayhiFeignClient {
    @RequestMapping(value = "hi",method = RequestMethod.GET)
    public String sayHello(@RequestParam(value = "name") String name);

    @RequestMapping(value = "tryTest1",method = RequestMethod.GET)
    public boolean tryTest1(@RequestParam(value = "name") String name);

    @RequestMapping(value = "test1",method = RequestMethod.GET)
    public String test1(@RequestParam(value = "name") String name);

    @RequestMapping(value = "test2",method = RequestMethod.GET)
    public String test2(@RequestParam(value = "name") String name);
}
