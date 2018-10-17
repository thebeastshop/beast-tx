/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 2018年10月12日
 */
package com.thebeastshop.tx.network.client;

import java.util.ServiceLoader;

import com.thebeastshop.tx.network.config.ClientConfig;

/**
 * 存储客户端提供者
 */
public class NetworkClientProvider {
	/**
	 * 创建存储客户端
	 * 
	 * @return
	 */
	public static NetworkClient create(ClientConfig config) {
		ServiceLoader<NetworkClient> loader = ServiceLoader.load(NetworkClient.class);
		if (loader != null && loader.iterator().hasNext()) {
			return loader.iterator().next().initClient(config);
		}
		return null;
	}
}
