/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/24
 */
package com.thebeastshop.tx.aop;

import com.thebeastshop.tx.constant.TxConstant;
import com.thebeastshop.tx.context.MethodDefinationManager;
import com.thebeastshop.tx.context.TxContext;
import com.thebeastshop.tx.enums.TxContextStateEnum;
import com.thebeastshop.tx.exceptions.RollbackException;
import com.thebeastshop.tx.exceptions.TransactionException;
import com.thebeastshop.tx.hook.CancelInvokeHook;
import com.thebeastshop.tx.socket.client.SocketClient;
import com.thebeastshop.tx.utils.InetUtils;
import com.thebeastshop.tx.utils.UniqueIdGenerator;
import com.thebeastshop.tx.vo.MonitorVo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ServiceLoader;
import java.util.function.Consumer;

/**
 * BeastTx注解切面
 */
@Aspect
public class TxAspect implements ApplicationContextAware, InitializingBean {

    private final Logger log = LoggerFactory.getLogger(TxAspect.class);

    private static ApplicationContext applicationContext = null;

    private SocketClient socketClient;

    @Pointcut("@annotation(com.thebeastshop.tx.annotation.BeastTx)")
    public void cut(){}

    @Around("cut()")
    public Object around(ProceedingJoinPoint jp) {
        MethodSignature signature = (MethodSignature)jp.getSignature();
        Class clazz = jp.getTarget().getClass();
        Method txMethod = signature.getMethod();
        Object[] txArgs = jp.getArgs();
        TxContext txContext = null;
        try {
            txContext = new TxContext(InetUtils.getAddress(),UniqueIdGenerator.generateId(),
                    clazz.getName(),
                    txMethod.getName(),
                    txArgs,
                    TxContextStateEnum.INIT);
            TransactionSynchronizationManager.bindResource(TxConstant.TRANSACTION_CONTEXT_KEY, txContext);
            log.info("[BEAST-TX]开启事务，事务ID[{}]",txContext.getTxId());

            Object result = jp.proceed();
            txContext.setTxContextState(TxContextStateEnum.SUCCESS);
            return result;
        } catch (Throwable t) {
            log.error("[BEAST-TX]事务调用发生错误",t);
            txContext.setExceptionMessage(t.getMessage());
            txContext.setTxContextState(TxContextStateEnum.ROLLBACKING);
            try{
                if(txContext.needRollback()){
                    log.info("[BEAST-TX]开始回滚事务，事务ID[{}]",txContext.getTxId());
                    txContext.rollback();
                    log.info("[BEAST-TX]回滚事务[{}]成功",txContext.getTxId());
                }
                txContext.setTxContextState(TxContextStateEnum.ROLLBACK_SUCCESS);
            }catch(RollbackException e){
                log.info("[BEAST-TX]回滚事务[{}]失败",txContext.getTxId());
                log.error(e.getMessage());
                txContext.setTxContextState(TxContextStateEnum.ROLLBACK_FAILED);
            }
            CancelInvokeHook cancelInvokeHook = MethodDefinationManager.getCancelInvokeHook();
            if(cancelInvokeHook != null){
                cancelInvokeHook.hookProcess(jp.getTarget().getClass(),txMethod.getName(),jp.getArgs(),t);
            }

            throw new TransactionException(t.getMessage());
        }finally {
            try{
                if(socketClient!=null){
                    MonitorVo monitorVo = convertToMonitorVo(txContext);
                    socketClient.send(monitorVo);
                }
            }catch(Exception e){
                log.error("[BEAST-TX]发送监控数据异常",e);
            }
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

    private MonitorVo convertToMonitorVo(TxContext context){
        MonitorVo monitorVo = new MonitorVo();
        monitorVo.setNodeId(context.getNodeId());
        monitorVo.setTxId(context.getTxId());
        monitorVo.setTxClassName(context.getTxClassName());
        monitorVo.setTxMethodName(context.getTxMethodName());
        monitorVo.setTxArgs(context.getTxArgs());
        monitorVo.setTxContextState(context.getTxContextState());
        return monitorVo;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        try{
            socketClient = TxAspect.getApplicationContext().getBean(SocketClient.class);
        }catch (Throwable t){
            log.warn("couldn't find finsockerClient instance in spring context");
        }
    }
}
