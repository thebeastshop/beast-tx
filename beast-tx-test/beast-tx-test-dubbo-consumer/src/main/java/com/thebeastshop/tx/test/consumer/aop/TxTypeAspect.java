package com.thebeastshop.tx.test.consumer.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class TxTypeAspect {
    @Pointcut("@within(org.springframework.transaction.annotation.Transactional)")
    public void cutClass(){}

    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional)")
    public void cutMethod(){}

    @Around("cutClass()")
    public Object around1(ProceedingJoinPoint thisJoinPoint){
        System.out.println("start");
        try {
            System.out.println("finish");
            return thisJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }

    @Around("cutMethod()")
    public Object around2(ProceedingJoinPoint thisJoinPoint){
        System.out.println("start");
        try {
            System.out.println("finish");
            return thisJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return null;
        }
    }
}
