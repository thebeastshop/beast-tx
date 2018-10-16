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
import com.thebeastshop.tx.enums.TxContextStateEnum;
import com.thebeastshop.tx.enums.TxTypeEnum;
import com.thebeastshop.tx.utils.InetUtils;
import com.thebeastshop.tx.utils.UniqueIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * BeastTx事务管理器
 */
public class TxTransactionManager extends DataSourceTransactionManager {

    private final Logger log = LoggerFactory.getLogger(TxTransactionManager.class);

    public static TxContext getTransactionContext() {
        return (TxContext) TransactionSynchronizationManager.getResource(TxConstant.TRANSACTION_CONTEXT_KEY);
    }

    public static long getTransactionId() {
        return (Long)TransactionSynchronizationManager.getResource(TxConstant.TRANSACTION_ID_KEY);
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        super.doBegin(transaction, definition);
        TxContext txContext = new TxContext(InetUtils.getEncodeAddress(), UniqueIdGenerator.generateId(),
                (TxTypeEnum) TransactionSynchronizationManager.getResource(TxConstant.TRANSACTION_TX_TYPE),
                TxContextStateEnum.INIT);
        log.info("[BEAST-TX]开启事务，事务ID[{}]，事务策略类型[{}]",txContext.getTxId(),txContext.getTxType());
        TransactionSynchronizationManager.bindResource(TxConstant.TRANSACTION_CONTEXT_KEY, txContext);
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        TxContext txContext = (TxContext) TransactionSynchronizationManager.getResource(TxConstant.TRANSACTION_CONTEXT_KEY);
        //如果为最终一致策略的时候。不进行回滚
        if(txContext.getTxType().equals(TxTypeEnum.TCC)){
            log.info("[BEAST-TX]开始回滚事务，事务ID[{}]，事务策略类型[{}]",txContext.getTxId(),txContext.getTxType());
            txContext.setTxContextState(TxContextStateEnum.ROLLBACKING);
            //TODO
            super.doRollback(status);
        }
    }

    @Override
    protected void doCleanupAfterCompletion(Object transaction) {
        super.doCleanupAfterCompletion(transaction);
        TransactionSynchronizationManager.unbindResource(TxConstant.TRANSACTION_CONTEXT_KEY);
        TransactionSynchronizationManager.unbindResource(TxConstant.TRANSACTION_TX_TYPE);
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        TxContext txContext = (TxContext) TransactionSynchronizationManager.getResource(TxConstant.TRANSACTION_CONTEXT_KEY);
        txContext.setTxContextState(TxContextStateEnum.SUCCESS);
        log.info("[BEAST-TX]开始回滚事务，事务ID[{}]，事务策略类型[{}]",txContext.getTxId(),txContext.getTxType());
        super.doCommit(status);
    }
}
