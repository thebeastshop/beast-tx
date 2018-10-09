package com.thebeastshop.tx.dubbo.spring;

import com.thebeastshop.tx.context.MethodDefinationManager;
import com.thebeastshop.tx.exceptions.DubboMethodScanException;
import com.thebeastshop.tx.exceptions.NoDubboDependencyException;
import com.thebeastshop.tx.utils.MethodUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

import java.lang.reflect.Method;

public class DubboMethodScanner implements BeanPostProcessor,PriorityOrdered, ApplicationContextAware {

    private final Logger log = LoggerFactory.getLogger(DubboMethodScanner.class);

    private static ApplicationContext applicationContext = null;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class referenceClass = null;
        try {
            referenceClass = Class.forName("com.alibaba.dubbo.config.spring.ReferenceBean");
        } catch (ClassNotFoundException e) {
            String errorMsg = "Cannot find dubbo dependency jar";
            log.error(errorMsg,e);
            throw new NoDubboDependencyException(errorMsg);
        }

        Class beanClass = bean.getClass();
        if(referenceClass.isAssignableFrom(beanClass)){
            try {
                //给dubbo设置filter
                Method method = beanClass.getMethod("setFilter",String.class);
                MethodUtil.invokeMethod(method,bean,new Object[]{"litxFilter"});
                //注册进方法定义管理器
                MethodDefinationManager.registerMethod(beanClass);
            } catch (Exception e) {
                String errorMsg = "An exception occurs in the scanning of the Dubbo method";
                log.error(errorMsg,e);
                throw new DubboMethodScanException(errorMsg);
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        DubboMethodScanner.applicationContext = applicationContext;
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
