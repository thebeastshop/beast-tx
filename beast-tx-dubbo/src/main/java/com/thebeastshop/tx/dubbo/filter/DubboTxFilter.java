/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/9
 */
package com.thebeastshop.tx.dubbo.filter;

import com.alibaba.dubbo.rpc.*;
import com.thebeastshop.tx.constant.TxConstant;
import com.thebeastshop.tx.context.MethodDefinationManager;
import com.thebeastshop.tx.context.TxContext;
import com.thebeastshop.tx.context.content.InvokeContent;
import com.thebeastshop.tx.context.content.MethodContent;
import com.thebeastshop.tx.enums.TxTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.lang.reflect.Method;

/**
 * DUBBO调用拦截器
 */
public class DubboTxFilter implements Filter {

    private final static Logger log = LoggerFactory.getLogger(DubboTxFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Result result = invoker.invoke(invocation);
        TxTypeEnum txType = (TxTypeEnum)TransactionSynchronizationManager.getResource(TxConstant.TRANSACTION_TX_TYPE);

        if(txType.equals(TxTypeEnum.TCC)){
            if(result == null || result.hasException()){
                return result;
            }
            TxContext txContext = (TxContext)TransactionSynchronizationManager.getResource(TxConstant.TRANSACTION_CONTEXT_KEY);
            if(txContext == null){
                return result;
            }
            Class interfaceClass = invoker.getInterface();
            String methodName = invocation.getMethodName();
            Class[] paramTypes = invocation.getParameterTypes();
            Method method = null;
            try {
                method = interfaceClass.getMethod(methodName,paramTypes);
            } catch (NoSuchMethodException e) {
                log.error("no such dubbo method[{}]",methodName);
                return result;
            }

            MethodContent methodContent = MethodDefinationManager.getMethodCentent(method);
            if(methodContent == null){
                return result;
            }

            InvokeContent invokeContent = new InvokeContent();
            invokeContent.setTxType(txType);
            invokeContent.setArgs(invocation.getArguments());
            invokeContent.setResult(result);
            invokeContent.setMethodContent(methodContent);
            txContext.logInvokeContent(invokeContent);
        }else if(txType.equals(TxTypeEnum.FINAL_CONSISTENCY)){

        }
        return result;
    }
}
