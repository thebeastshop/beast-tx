/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/9
 */
package com.thebeastshop.tx.enums;

/**
 * 事务上下文状态
 */
public enum TxContextStateEnum {
    SUCCESS,
    RETRYING,
    RETRY_SUCCESS,
    RETRY_FAILED,
    ROLLBACKING,
    ROLLBACK_SUCCESS,
    ROLLBACK_FAILED
}
