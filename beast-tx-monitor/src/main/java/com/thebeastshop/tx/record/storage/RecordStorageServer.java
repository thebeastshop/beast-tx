/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 
 */
package com.thebeastshop.tx.record.storage;

/**
 * 记录存储服务端
 */
public interface RecordStorageServer {
	/**
	 * 启动程序入口
	 */
	void start();
}
