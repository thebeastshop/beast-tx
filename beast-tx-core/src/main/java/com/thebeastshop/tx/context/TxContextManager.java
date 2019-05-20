/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2019-05-17
 */
package com.thebeastshop.tx.context;

/**
 * TxContext管理器
 */
public class TxContextManager {

    private static ThreadLocal<TxContext> txContextTL = new InheritableThreadLocal<>();

    public static void bindTxContext(TxContext txContext){
        txContextTL.set(txContext);
    }

    public static void unbindTxContext(){
        txContextTL.remove();
    }

    public static TxContext getTxContext(){
        return txContextTL.get();
    }

    public static boolean hasTxContent(){
        return getTxContext() != null;
    }
}
