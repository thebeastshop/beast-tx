/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/13
 */
package com.thebeastshop.tx.proxy;


import com.thebeastshop.tx.enums.TxTypeEnum;
import com.thebeastshop.tx.transaction.TxTypeThreadLocalManager;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 检测事务策略动态代理处理类
 */
public class TxTypeProxyHandler implements InvocationHandler {

    private Object bean;

    private Map<Method, TxTypeEnum> methodTxTypeMap = new HashMap<>();

    public TxTypeProxyHandler(Map<Method, TxTypeEnum> methodTxTypeMap,Object bean) {
        this.methodTxTypeMap = methodTxTypeMap;
        this.bean = bean;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        TxTypeEnum txType = methodTxTypeMap.get(method);
        if(txType != null){
            System.out.println("-------" + txType + "-------");
            TxTypeThreadLocalManager.put(txType);
        }
        return method.invoke(bean,args);
    }
}
