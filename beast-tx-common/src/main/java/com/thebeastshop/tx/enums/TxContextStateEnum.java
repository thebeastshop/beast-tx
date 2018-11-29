/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/9
 */
package com.thebeastshop.tx.enums;

/**
 * 事务上下文状态枚举
 */
public enum TxContextStateEnum {
    INIT,
    SUCCESS,
    RETRYING,
    RETRY_SUCCESS,
    RETRY_FAILED,
    ROLLBACKING,
    ROLLBACK_SUCCESS,
    ROLLBACK_FAILED
}
