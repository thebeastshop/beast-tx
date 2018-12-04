/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 2018年10月12日
 */
package com.thebeastshop.tx.socket.client;

import java.util.ServiceLoader;

import com.thebeastshop.tx.socket.config.ClientConfig;

/**
 * 存储客户端提供者
 */
public class SocketClientProvider {
	/**
	 * 创建存储客户端
	 * 
	 * @return
	 */
	public static SocketClient create(ClientConfig config) {
		ServiceLoader<SocketClient> loader = ServiceLoader.load(SocketClient.class);
		if (loader != null && loader.iterator().hasNext()) {
			return loader.iterator().next().initClient(config);
		}
		return null;
	}
}
