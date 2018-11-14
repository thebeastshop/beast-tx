package com.thebeastshop.tx.test.sc.consumer.config;

import com.thebeastshop.tx.aop.TxAspect;
import com.thebeastshop.tx.feign.aop.FeignTxAspect;
import com.thebeastshop.tx.feign.interceptor.FeignInterceptor;
import com.thebeastshop.tx.feign.spring.FeignMethodScanner;
import com.thebeastshop.tx.test.sc.consumer.constant.TxConsumerConstant;
import feign.InvocationHandlerFactory;
import feign.Request;
import feign.Retryer;
import feign.Target;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

@Configuration
public class BeanConfiguration {

    @Autowired
    private TxConsumerConstant txConsumerConstant;

    @Bean
    public Request.Options options() {
        return new Request.Options(txConsumerConstant.getConnectTimeOutMillis(), txConsumerConstant.getReadTimeOutMillis());
    }

    @Bean
    public Retryer feignRetryer() {
        return Retryer.NEVER_RETRY;
    }

    @Bean
    public FeignMethodScanner feignMethodScanner(){
        FeignMethodScanner scanner = new FeignMethodScanner();
        scanner.setFeignPackage(txConsumerConstant.getTxfeignScanPackage());
        return scanner;
    }

    @Bean
    public TxAspect txAspect(){
        return new TxAspect();
    }

    @Bean
    public FeignTxAspect feignTxAspect(){
        return new FeignTxAspect();
    }

}
