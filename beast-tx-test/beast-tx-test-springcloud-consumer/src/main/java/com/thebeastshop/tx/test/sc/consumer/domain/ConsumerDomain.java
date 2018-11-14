package com.thebeastshop.tx.test.sc.consumer.domain;

import com.thebeastshop.tx.annotation.BeastTx;
import com.thebeastshop.tx.test.sc.consumer.feign.TxTestSayhiFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ConsumerDomain {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TxTestSayhiFeignClient txTestSayhiFeignClient;

    @BeastTx
    public String doTest(String name){
        jdbcTemplate.execute("insert into user (name, age) values ('beast-tx', '99131')");

        String result = txTestSayhiFeignClient.test1("jack");
        System.out.println("result:" + result);
        txTestSayhiFeignClient.test2("rose");
        return "ok";
    }
}
