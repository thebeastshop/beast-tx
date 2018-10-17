/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 2018年10月12日
 */
package com.thebeastshop.tx.network.tio.server;

import org.tio.server.ServerGroupContext;
import org.tio.server.TioServer;
import org.tio.server.intf.ServerAioListener;

import com.thebeastshop.tx.network.config.ServerConfig;
import com.thebeastshop.tx.network.server.NetworkServer;

/**
 * 用t-io通信框架实现服务端
 */
public class NetworkTioServer implements NetworkServer {

	// tioServer对象
	public static TioServer tioServer;

	// 有时候需要绑定ip，不需要则null
	private String serverIp = null;

	// 监听的端口
	private int serverPort = 6789;

	// 心跳超时时间
	public static final int timeout = 5000;
	
	@Override
	public NetworkServer initServer(ServerConfig config) {
		serverIp = config.getIp();
		serverPort = config.getPort();
		// handler, 包括编码、解码、消息处理
		NetworkServerAioHandler aioHandler = new NetworkServerAioHandler();
		aioHandler.handler = config.getHandler();
		// 事件监听器，可以为null，但建议自己实现该接口，可以参考showcase了解些接口
		ServerAioListener aioListener = null;
		// 一组连接共用的上下文对象
		ServerGroupContext serverGroupContext = new ServerGroupContext("", aioHandler, aioListener);
		serverGroupContext.setHeartbeatTimeout(timeout);
		tioServer = new TioServer(serverGroupContext);
		return this;
	}

	@Override
	public void start() {
		try {
			tioServer.start(serverIp, serverPort);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}