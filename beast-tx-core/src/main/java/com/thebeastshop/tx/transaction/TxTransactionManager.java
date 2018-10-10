/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/9
 */
package com.thebeastshop.tx.transaction;

import com.thebeastshop.tx.constant.TxConstant;
import com.thebeastshop.tx.context.TxContext;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * BeastTx事务管理器
 */
public class TxTransactionManager extends DataSourceTransactionManager {

    public static TxContext getTransactionContext() {
        return (TxContext) TransactionSynchronizationManager.getResource(TxConstant.TRANSACTION_CONTEXT_KEY);
    }

    public static long getTransactionId() {
        return (Long)TransactionSynchronizationManager.getResource(TxConstant.TRANSACTION_ID_KEY);
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        super.doBegin(transaction, definition);
    }
}
