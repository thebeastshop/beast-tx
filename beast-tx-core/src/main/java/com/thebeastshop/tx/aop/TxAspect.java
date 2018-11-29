/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/24
 */
package com.thebeastshop.tx.aop;

import com.thebeastshop.tx.annotation.BeastTx;
import com.thebeastshop.tx.constant.TxConstant;
import com.thebeastshop.tx.context.TxContext;
import com.thebeastshop.tx.enums.TxContextStateEnum;
import com.thebeastshop.tx.exceptions.RollbackException;
import com.thebeastshop.tx.exceptions.TransactionException;
import com.thebeastshop.tx.utils.InetUtils;
import com.thebeastshop.tx.utils.UniqueIdGenerator;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import java.lang.reflect.Method;

/**
 * BeastTx注解切面
 */
@Aspect
public class TxAspect implements ApplicationContextAware {

    private final Logger log = LoggerFactory.getLogger(TxAspect.class);

    private static ApplicationContext applicationContext = null;

    @Pointcut("@annotation(com.thebeastshop.tx.annotation.BeastTx)")
    public void cut(){}

    @Around("cut()")
    public Object around(ProceedingJoinPoint jp) {
        try {
            MethodSignature signature = (MethodSignature)jp.getSignature();
            Method method = signature.getMethod();
            BeastTx annotation = method.getAnnotation(BeastTx.class);

            TxContext txContext = new TxContext(InetUtils.getEncodeAddress(), UniqueIdGenerator.generateId(),
                    TxContextStateEnum.INIT);
            TransactionSynchronizationManager.bindResource(TxConstant.TRANSACTION_CONTEXT_KEY, txContext);
            log.info("[BEAST-TX]开启事务，事务ID[{}]",txContext.getTxId());

            Object result = jp.proceed();
            txContext.setTxContextState(TxContextStateEnum.SUCCESS);
            return result;
        } catch (Throwable t) {
            log.error("[BEAST-TX]事务调用发生错误",t);
            TxContext txContext = (TxContext) TransactionSynchronizationManager.getResource(TxConstant.TRANSACTION_CONTEXT_KEY);
            txContext.setTxContextState(TxContextStateEnum.ROLLBACKING);
            try{
                if(txContext.needRollback()){
                    log.info("[BEAST-TX]开始回滚事务，事务ID[{}]",txContext.getTxId());
                    txContext.rollback();
                    log.info("[BEAST-TX]回滚事务[{}]成功",txContext.getTxId());
                }
            }catch(RollbackException e){
                log.info("[BEAST-TX]回滚事务[{}]失败",txContext.getTxId());
                log.error(e.getMessage());
                txContext.setTxContextState(TxContextStateEnum.ROLLBACK_FAILED);
            }
            txContext.setTxContextState(TxContextStateEnum.ROLLBACK_SUCCESS);
            throw new TransactionException(t.getMessage());
        }finally {
            TransactionSynchronizationManager.unbindResource(TxConstant.TRANSACTION_CONTEXT_KEY);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        TxAspect.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext(){
        return TxAspect.applicationContext;
    }
}
