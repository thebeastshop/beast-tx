package com.thebeastshop.tx.test.consumer.service.impl;

import com.thebeastshop.tx.test.consumer.service.TestService;
import com.thebeastshop.tx.test.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

@Transactional
@Service("testService")
public class TestServiceImpl implements TestService {

    private final static Logger log = LoggerFactory.getLogger(TestServiceImpl.class);

    @Resource
    private DemoService demoService;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Override
    public void consumerTest() {
        jdbcTemplate.execute("insert into user (name, age) values ('beast-tx', '99131')");

        String result = demoService.test1("jack");
        System.out.println("result:" + result);
        demoService.test3(8L);
    }
}
