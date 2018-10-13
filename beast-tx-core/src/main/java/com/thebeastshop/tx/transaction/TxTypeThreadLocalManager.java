/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/13
 */
package com.thebeastshop.tx.transaction;

import com.thebeastshop.tx.enums.TxTypeEnum;

/**
 * 事务策略的ThreadLocal管理器
 * 用于在事务中传递事务策略
 */
public class TxTypeThreadLocalManager {

    private static ThreadLocal<TxTypeEnum> txTypeThreadLocal = new ThreadLocal<>();

    public static void put(TxTypeEnum txType){
        txTypeThreadLocal.set(txType);
    }

    public static TxTypeEnum get(){
        return txTypeThreadLocal.get();
    }

    public static void remove(){
        txTypeThreadLocal.remove();
    }
}
