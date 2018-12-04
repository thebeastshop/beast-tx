package com.thebeastshop.tx.test.sc.consumer.hook;

import com.thebeastshop.tx.hook.CancelInvokeHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DemoHook implements CancelInvokeHook {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public void hookProcess(Class clazz, String method, Object[] args, Throwable cancelException) {
        log.info("hook invoked");
        log.info("clazz:" + clazz.getName());
        log.info("method:" + method);
        log.info("args:" + args);
        log.info("exception:" + cancelException.getMessage());
    }
}
