package com.thebeastshop.tx.test.consumer.service.impl;

import com.thebeastshop.tx.test.consumer.service.TestService;
import com.thebeastshop.tx.test.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

@Service("testService")
@Transactional
public class TestServiceImpl implements TestService {

    private final static Logger log = LoggerFactory.getLogger(TestServiceImpl.class);

    @Resource
    private DemoService demoService;

    @Override
    public void consumerTest() {
        //DO DB
        String result = demoService.test1("jack");
        System.out.println("result:" + result);
    }
}
