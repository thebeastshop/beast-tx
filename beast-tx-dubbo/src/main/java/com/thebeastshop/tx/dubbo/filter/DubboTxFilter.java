/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/9
 */
package com.thebeastshop.tx.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.google.common.collect.Lists;
import com.thebeastshop.tx.context.MethodDefinationManager;
import com.thebeastshop.tx.context.TxContext;
import com.thebeastshop.tx.context.TxContextManager;
import com.thebeastshop.tx.context.content.InvokeContent;
import com.thebeastshop.tx.context.content.MethodContent;
import com.thebeastshop.tx.dubbo.spring.DubboMethodScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.text.MessageFormat;

/**
 * DUBBO调用拦截器
 */
@Activate(group = {Constants.CONSUMER},order = -1000)
public class DubboTxFilter implements Filter {

    private final static Logger log = LoggerFactory.getLogger(DubboTxFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        TxContext txContext = null;
        if(TxContextManager.hasTxContent()){
            txContext = TxContextManager.getTxContext();
        }

        Method method = null;
        try {
            method = getInvokeMethod(invoker, invocation);
        } catch (NoSuchMethodException e) {
            log.error("找不到DUBBO方法[{}]",invocation.getMethodName());
            throw new RpcException();
        }

        MethodContent methodContent = MethodDefinationManager.getMethodCentent(method);

        if(txContext != null && methodContent != null &&
                Lists.newArrayList(MethodContent.MethodContentState.TCC,MethodContent.MethodContentState.TC).contains(methodContent.getMethodContentState())){
            log.info("[BEAST-TX]事务ID[{}],开始执行接口[{}]的TRY方法[{}]",
                    txContext.getTxId(),
                    invoker.getInterface().getName(),
                    methodContent.getTryMethod().getName());
            Object bean = DubboMethodScanner.getApplicationContext().getBean(invoker.getInterface());
            try {
                boolean tryFlag = (Boolean) methodContent.getTryMethod().invoke(bean,invocation.getArguments());
                if(!tryFlag){
                    String errorMsg = MessageFormat.format("[BEAST-TX]事务ID[{0}],执行接口[{1}]的TRY方法[{2}]出现异常",
                            txContext.getTxId(),
                            invoker.getInterface().getName(),
                            methodContent.getTryMethod().getName());
                    throw new RpcException(errorMsg);
                }
            } catch (Exception e) {
                String errorMsg = MessageFormat.format("[BEAST-TX]事务ID[{0}],执行接口[{1}]的TRY方法[{2}]出现异常",
                        txContext.getTxId().toString(),
                        invoker.getInterface().getName(),
                        methodContent.getTryMethod().getName());
                throw new RpcException(errorMsg);
            }
        }

        Result result = invoker.invoke(invocation);

        if(txContext == null){
            return result;
        }

        if(methodContent == null){
            return result;
        }

        if(result == null || result.hasException()){
            return result;
        }
        InvokeContent invokeContent = getInvokeContent(invoker,invocation,methodContent,txContext,result);
        txContext.logInvokeContent(invokeContent);

        return result;
    }

    private Method getInvokeMethod(Invoker<?> invoker, Invocation invocation) throws NoSuchMethodException{
        Class interfaceClass = invoker.getInterface();
        String methodName = invocation.getMethodName();
        Class[] paramTypes = invocation.getParameterTypes();
        return interfaceClass.getMethod(methodName,paramTypes);
    }

    private InvokeContent getInvokeContent(Invoker<?> invoker, Invocation invocation,
                                           MethodContent methodContent, TxContext txContext,Result result){
        Class interfaceClass = invoker.getInterface();

        InvokeContent invokeContent = new InvokeContent();
        invokeContent.setInterfaceClass(interfaceClass);
        invokeContent.setTxId(txContext.getTxId());
        invokeContent.setArgs(invocation.getArguments());
        invokeContent.setResult(result.getValue());
        invokeContent.setMethodContent(methodContent);
        return invokeContent;
    }
}
