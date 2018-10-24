/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/24
 */
package com.thebeastshop.tx.aop;

import com.thebeastshop.tx.annotation.BeastTx;
import com.thebeastshop.tx.constant.TxConstant;
import com.thebeastshop.tx.context.TxContext;
import com.thebeastshop.tx.enums.TxContextStateEnum;
import com.thebeastshop.tx.enums.TxTypeEnum;
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
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * BeastTx注解切面
 */
@Aspect
public class TxAspect {

    private final Logger log = LoggerFactory.getLogger(TxAspect.class);

    @Pointcut("@annotation(com.thebeastshop.tx.annotation.BeastTx)")
    public void cut(){}

    @Around("cut()")
    public Object around(ProceedingJoinPoint jp) {
        try {
            MethodSignature signature = (MethodSignature)jp.getSignature();
            Method method = signature.getMethod();
            BeastTx annotation = method.getAnnotation(BeastTx.class);

            TxContext txContext = new TxContext(InetUtils.getEncodeAddress(), UniqueIdGenerator.generateId(),
                    annotation.value(),
                    TxContextStateEnum.INIT);
            TransactionSynchronizationManager.bindResource(TxConstant.TRANSACTION_CONTEXT_KEY, txContext);
            log.info("[BEAST-TX]开启事务，事务ID[{}],事务策略类型[{}]",txContext.getTxId(),txContext.getTxType().getValue());
            Object result = jp.proceed();
            return result;
        } catch (Throwable t) {
            log.error("[BEAST-TX]事务调用发生错误",t);
            TxContext txContext = (TxContext) TransactionSynchronizationManager.getResource(TxConstant.TRANSACTION_CONTEXT_KEY);
            //如果为最终一致策略的时候。不进行回滚
            if(txContext.getTxType().equals(TxTypeEnum.TCC)){
                log.info("[BEAST-TX]开始回滚事务，事务ID[{}]，事务策略类型[{}]",txContext.getTxId(),txContext.getTxType());
                txContext.setTxContextState(TxContextStateEnum.ROLLBACKING);
                try{
                    txContext.rollback();
                }catch(RollbackException e){
                    log.error(e.getMessage());
                    txContext.setTxContextState(TxContextStateEnum.ROLLBACK_FAILED);
                }
                txContext.setTxContextState(TxContextStateEnum.ROLLBACK_SUCCESS);
            }
            throw new TransactionException(t.getMessage());
        }finally {
            TransactionSynchronizationManager.unbindResource(TxConstant.TRANSACTION_CONTEXT_KEY);
        }
    }
}
