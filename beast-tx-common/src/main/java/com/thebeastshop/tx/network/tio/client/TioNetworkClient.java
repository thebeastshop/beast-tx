/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 2018年10月12日
 */
package com.thebeastshop.tx.network.tio.client;

import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Node;
import org.tio.core.Tio;

import com.thebeastshop.tx.network.HasBytes;
import com.thebeastshop.tx.network.client.NetworkClient;
import com.thebeastshop.tx.network.config.ClientConfig;
import com.thebeastshop.tx.network.tio.NetworkPacket;

/**
 * 用t-io通信框架实现客户端
 */
public class TioNetworkClient implements NetworkClient {

	private ClientChannelContext clientChannelContext;

	@Override
	public NetworkClient initClient(ClientConfig config) {
		NetworkClientAioHandler tioClientHandler = new NetworkClientAioHandler();
		tioClientHandler.handler = config.getHandler();
		ClientAioListener aioListener = null;
		ReconnConf reconnConf = new ReconnConf(5000L);
		ClientGroupContext clientGroupContext = new ClientGroupContext(tioClientHandler, aioListener, reconnConf);
		clientGroupContext.setHeartbeatTimeout(config.getTimeout());
		TioClient tioClient;
		try {
			tioClient = new TioClient(clientGroupContext);
			Node serverNode = new Node(config.getIp(), config.getPort());
			clientChannelContext = tioClient.connect(serverNode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public void send(HasBytes t) {
		NetworkPacket packet = new NetworkPacket();
		packet.setBody(t.toBytes());
		Tio.send(clientChannelContext, packet);
	}

}
