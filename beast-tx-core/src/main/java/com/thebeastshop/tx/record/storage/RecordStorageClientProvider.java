/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 
 */
package com.thebeastshop.tx.record.storage;

import java.util.ServiceLoader;

/**
 * 记录存储客户端提供者
 */
public class RecordStorageClientProvider {
	/**
	 * 创建记录存储客户端
	 * 
	 * @return
	 */
	public static RecordStorageClient create() {
		ServiceLoader<RecordStorageClient> loader = ServiceLoader.load(RecordStorageClient.class);
		if (loader != null && loader.iterator().hasNext()) {
			return loader.iterator().next();
		}
		return null;
	}
}
