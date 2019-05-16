package com.thebeastshop.tx.feign.spring;

import com.google.common.collect.Maps;
import com.thebeastshop.tx.context.MethodDefinationManager;
import com.thebeastshop.tx.feign.aop.FeignTxAspect;
import com.thebeastshop.tx.hook.CancelInvokeHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;
import java.util.Map;

public class FeignMethodScanner implements SmartInitializingSingleton, ApplicationContextAware {

    private final static Logger log = LoggerFactory.getLogger(FeignMethodScanner.class);

    private static ApplicationContext applicationContext;

    @Override
    public void afterSingletonsInstantiated() {
        Map<String,Object> map = applicationContext.getBeansWithAnnotation(FeignClient.class);
        map.forEach((s, o) -> MethodDefinationManager.registerMethod(getFeignInterface(o)));
        CancelInvokeHook cancelInvokeHook = applicationContext.getBean(CancelInvokeHook.class);
        if(cancelInvokeHook != null){
            MethodDefinationManager.registerCancelInvokeHook(cancelInvokeHook);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    private Class getFeignInterface(Object proxy){
        try {
            Field field = proxy.getClass().getSuperclass().getDeclaredField("h");
            field.setAccessible(true);
            Object h = field.get(proxy);
            field = h.getClass().getDeclaredField("advised");
            field.setAccessible(true);
            AdvisedSupport advised = (AdvisedSupport)field.get(h);
            Class feignClass = advised.getProxiedInterfaces()[0];
            return feignClass;
        }catch (Throwable t){
            log.error("[BEAST-TX]获取Feign接口类出错",t);
            return null;
        }
    }
}
