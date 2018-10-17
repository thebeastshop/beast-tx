/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/16
 */
package com.thebeastshop.tx.dubbo.invoke;

import com.thebeastshop.tx.context.content.InvokeContent;
import com.thebeastshop.tx.context.content.MethodContent;
import com.thebeastshop.tx.dubbo.spring.DubboMethodScanner;
import com.thebeastshop.tx.exceptions.RollbackException;
import com.thebeastshop.tx.scan.demo.Demo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * dubbo调用内容实现类
 */
public class DubboInvokeContent extends InvokeContent {

    private final static Logger log = LoggerFactory.getLogger(DubboInvokeContent.class);

    @Override
    public void rollback() {
        if(this.getMethodContent().getMethodContentState().equals(MethodContent.MethodContentState.CC)
                || this.getMethodContent().getMethodContentState().equals(MethodContent.MethodContentState.TCC)){
            Object bean = DubboMethodScanner.getApplicationContext().getBean(this.getInterfaceClass());

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
}
