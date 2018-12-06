/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 2018年10月12日
 */
package com.thebeastshop.tx.socket.client;

import com.thebeastshop.tx.socket.config.ClientConfig;

/**
 * 客户端
 */
public interface SocketClient {

	SocketClient initClient(ClientConfig config);

	<T> void send(T t);

}
