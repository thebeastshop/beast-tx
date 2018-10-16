/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 2018年10月16日
 */
package com.thebeastshop.tx.storage;

/**
 * 
 */
public interface HandlerCallback {
	
	void doCallback(byte[] dataBytes);

}
