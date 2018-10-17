package com.thebeastshop.tx.test.service.impl;

import com.thebeastshop.tx.test.service.DemoService;
import org.springframework.stereotype.Service;

@Service("demoService")
public class DemoServiceImpl implements DemoService {
    @Override
    public String tryTest1(String name) {
        System.out.println("tryTest1");
        return "hello," + name;
    }

    @Override
    public String test1(String name) {
        System.out.println("test1");
        return "hello," + name;
    }

    @Override
    public String test2(String name, Integer age) {
        System.out.println("test2");
        return "hello," + name + ".age:" + age;
    }

    @Override
    public String test3(Long id) {
        System.out.println("test3");
        return "hello,id:" + id;
    }

    @Override
    public void cancelTest1(String str) {
        System.out.println("cancel test1");
    }

    @Override
    public void cancelTest2(String str) {
        System.out.println("cancel test2");
    }
}
