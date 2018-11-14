package com.thebeastshop.tx.feign.interceptor;


import feign.InvocationHandlerFactory;
import feign.Target;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

public class FeignInterceptor implements InvocationHandler {

    private Target target;

    private Map<Method, InvocationHandlerFactory.MethodHandler> handlers;

    public FeignInterceptor(Target target, Map<Method, InvocationHandlerFactory.MethodHandler> handlers) {
        this.target = target;
        this.handlers = handlers;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("test");
        return null;
    }
}
