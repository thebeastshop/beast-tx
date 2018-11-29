/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/9/30
 */
package com.thebeastshop.tx.context.content;

import com.thebeastshop.tx.aop.TxAspect;
import com.thebeastshop.tx.exceptions.RollbackException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;

/**
 * 调用内容
 */
public class InvokeContent {

    private final Logger log = LoggerFactory.getLogger(InvokeContent.class);

    private Class interfaceClass;

    private Long txId;

    private MethodContent methodContent;

    private Object[] args;

    private Object result;

    public void rollback() {
        if(this.getMethodContent().getMethodContentState().equals(MethodContent.MethodContentState.CC)
                || this.getMethodContent().getMethodContentState().equals(MethodContent.MethodContentState.TCC)){
            Object bean = TxAspect.getApplicationContext().getBean(this.getInterfaceClass());

            try {
                log.info("[BEAST-TX]事务ID[{}]，开始调用接口[{}]的回滚方法[{}]",
                        this.getTxId(),this.getInterfaceClass().getName(),
                        this.getMethodContent().getCancelMethod().getName());
                try {
                    this.getMethodContent().getCancelMethod().invoke(bean, this.getArgs());
                } catch (IllegalArgumentException e){
                    try{
                        this.getMethodContent().getCancelMethod().invoke(bean,this.getArgs(),this.getResult());
                    }catch (Exception e1){
                        throw e1;
                    }
                } catch (IllegalAccessException e) {
                    throw e;
                } catch (InvocationTargetException e) {
                    throw e;
                }
            } catch (Exception e) {
                String errorMsg = MessageFormat.format("[BEAST-TX]事务ID[{0}],调用接口[{1}]的回滚方法[{2}]产生异常",
                        this.getTxId(),
                        this.getInterfaceClass().getName(),
                        this.getMethodContent().getCancelMethod().getName());
                log.error(errorMsg,e);
                throw new RollbackException(errorMsg);
            }
        }else{
            log.warn("[BEAST-TX]事务ID[{}]，没有找到接口[{}]中方法[{}]的回滚方法，不回滚",
                    this.getTxId(),
                    this.getInterfaceClass(),
                    this.getMethodContent().getConfirmMethod().getName());
        }
    }

    public Class getInterfaceClass() {
        return interfaceClass;
    }

    public void setInterfaceClass(Class interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public Long getTxId() {
        return txId;
    }

    public void setTxId(Long txId) {
        this.txId = txId;
    }

    public MethodContent getMethodContent() {
        return methodContent;
    }

    public void setMethodContent(MethodContent methodContent) {
        this.methodContent = methodContent;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
