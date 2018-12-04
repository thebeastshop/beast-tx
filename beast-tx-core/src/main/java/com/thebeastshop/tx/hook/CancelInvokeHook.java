/**
 * <p>Title: beast-tx</p>
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p>
 * @author Bryan.Zhang
 * @email weenyc31@163.com
 * @Date 2018/10/9
 */
package com.thebeastshop.tx.hook;

/**
 * 取消Hook接口
 */
public interface CancelInvokeHook {
	public void hookProcess(Class clazz, String method, Object[] args, Throwable cancelException);
}
