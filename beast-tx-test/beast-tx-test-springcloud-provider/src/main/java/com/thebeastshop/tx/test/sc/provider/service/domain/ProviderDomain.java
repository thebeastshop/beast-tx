package com.thebeastshop.tx.test.sc.provider.service.domain;

import org.springframework.stereotype.Component;

@Component
public class ProviderDomain {
    public boolean tryTest1(String name) {
        return true;
    }

    public String test1(String name) {
        return "hi," + name;
    }

    public String test2(String name) {
        if(1==1){
            throw new RuntimeException("test error");
        }
        return "bye," + name;
    }


}
