/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/11/6
 */
package com.thebeastshop.tx.feign.aop;

import com.thebeastshop.tx.constant.TxConstant;
import com.thebeastshop.tx.context.MethodDefinationManager;
import com.thebeastshop.tx.context.TxContext;
import com.thebeastshop.tx.context.content.InvokeContent;
import com.thebeastshop.tx.context.content.MethodContent;
import com.thebeastshop.tx.enums.TxTypeEnum;
import com.thebeastshop.tx.feign.exceptions.FeignException;
import com.thebeastshop.tx.feign.spring.FeignMethodScanner;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;
import java.text.MessageFormat;

@Component
@Aspect
public class FeignTxAspect {

    private final static Logger log = LoggerFactory.getLogger(FeignTxAspect.class);

    @Pointcut("@within(org.springframework.cloud.netflix.feign.FeignClient)")
    public void cut(){}

    @Around("cut()")
    public Object around(ProceedingJoinPoint jp){
        TxContext txContext = null;
        if(TransactionSynchronizationManager.hasResource(TxConstant.TRANSACTION_CONTEXT_KEY)){
            txContext = (TxContext)TransactionSynchronizationManager.getResource(TxConstant.TRANSACTION_CONTEXT_KEY);
        }

        MethodSignature signature = (MethodSignature)jp.getSignature();
        Method method = signature.getMethod();

        MethodContent methodContent = MethodDefinationManager.getMethodCentent(method);

        Class interfaceClass = jp.getSignature().getDeclaringType();

        if(txContext != null && methodContent != null &&
                methodContent.getMethodContentState().equals(MethodContent.MethodContentState.TCC)){

            log.info("[BEAST-TX]事务ID[{}],开始执行接口[{}]的TRY方法[{}]",
                    txContext.getTxId(),
                    interfaceClass.getName(),
                    methodContent.getTryMethod().getName());
            Object bean = FeignMethodScanner.getApplicationContext().getBean(interfaceClass);
            try {
                boolean tryFlag = (Boolean) methodContent.getTryMethod().invoke(bean,jp.getArgs());
                if(!tryFlag){
                    String errorMsg = MessageFormat.format("[BEAST-TX]事务ID[{0}],执行接口[{1}]的TRY方法[{2}]出现异常",
                            txContext.getTxId(),
                            interfaceClass.getName(),
                            methodContent.getTryMethod().getName());
                    throw new FeignException(errorMsg);
                }
            } catch (Exception e) {
                String errorMsg = MessageFormat.format("[BEAST-TX]事务ID[{0}],执行接口[{1}]的TRY方法[{2}]出现异常",
                        txContext.getTxId().toString(),
                        interfaceClass.getName(),
                        methodContent.getTryMethod().getName());
                throw new FeignException(errorMsg);
            }
        }

        boolean hasException = false;
        Object result = null;
        try{
            result = jp.proceed();
        }catch (Throwable t){
            log.error(t.getMessage(),t);
            hasException = true;
        }

        if(!hasException){
            if(txContext == null){
                return result;
            }

            if(methodContent == null){
                return result;
            }
        }

        TxTypeEnum txType = txContext.getTxType();
        if(txType.equals(TxTypeEnum.TCC)){
            InvokeContent invokeContent = getInvokeContent(interfaceClass,jp.getArgs(),methodContent,txContext,result);
            txContext.logInvokeContent(invokeContent);
        }else if(txType.equals(TxTypeEnum.FINAL_CONSISTENCY)){
            if(hasException){
                InvokeContent invokeContent = getInvokeContent(interfaceClass,jp.getArgs(),methodContent,txContext,null);
                txContext.logInvokeContent(invokeContent);
            }
        }

        if (hasException){
            String errorMsg = MessageFormat.format("[BEAST-TX]事务ID[{0}],执行接口[{1}]方法[{2}]出现异常",
                    txContext.getTxId().toString(),
                    interfaceClass.getName(),
                    methodContent.getTryMethod().getName());
            throw new FeignException(errorMsg);
        }
        return result;
    }

    private InvokeContent getInvokeContent(Class interfaceClass, Object[] args,
                                           MethodContent methodContent, TxContext txContext,Object result){
        InvokeContent invokeContent = new InvokeContent();
        invokeContent.setInterfaceClass(interfaceClass);
        invokeContent.setTxId(txContext.getTxId());
        invokeContent.setTxType(txContext.getTxType());
        invokeContent.setArgs(args);
        invokeContent.setResult(result);
        invokeContent.setMethodContent(methodContent);
        return invokeContent;
    }
}
