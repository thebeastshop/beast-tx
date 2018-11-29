package com.thebeastshop.tx.test.service.impl;

import com.thebeastshop.tx.test.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("demoService")
public class DemoServiceImpl implements DemoService {

    private final static Logger log = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Override
    public boolean tryTest1(String name) {
        log.info("tryTest1");
        return true;
    }

    @Override
    public String test1(String name) {
        log.info("test1");
        return "hello," + name;
    }

    @Override
    public String test2(String name, Integer age) {
        log.info("test2");
        return "hello," + name + ".age:" + age;
    }

    @Override
    public String test3(Long id) {
        log.info("test3");
        if(1==1){
            throw new RuntimeException("test error");
        }
        return "hello,id:" + id;
    }

    @Override
    public void cancelTest1(String str) {
        log.info("cancel test1");
    }

    @Override
    public void cancelTest2(String str) {
        log.info("cancel test2");
    }
}
