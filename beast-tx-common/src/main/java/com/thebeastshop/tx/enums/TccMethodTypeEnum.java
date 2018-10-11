/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/8
 */
package com.thebeastshop.tx.enums;

/**
 * Tcc方法类型枚举
 */
public enum TccMethodTypeEnum {
    TRY,CONFIRM,CANCEL
}
