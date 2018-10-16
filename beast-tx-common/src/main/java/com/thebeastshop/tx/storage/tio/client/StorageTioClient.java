/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 2018年10月12日
 */
package com.thebeastshop.tx.storage.tio.client;

import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Node;
import org.tio.core.Tio;

import com.alibaba.fastjson.JSON;
import com.thebeastshop.tx.storage.HandlerCallback;
import com.thebeastshop.tx.storage.client.StorageClient;
import com.thebeastshop.tx.storage.tio.StoragePacket;
import com.thebeastshop.tx.utils.UniqueIdGenerator;

/**
 * 用t-io通信框架实现记录存储客户端
 */
public class StorageTioClient implements StorageClient {

	private ClientChannelContext clientChannelContext;

	private StorageClientAioHandler tioClientHandler = new StorageClientAioHandler();

	private String serverIp = null;

	private int serverPort = 6789;

	private int timeout = 5000;

	public StorageTioClient() {
		super();
		ClientAioListener aioListener = null;
		ReconnConf reconnConf = new ReconnConf(5000L);
		ClientGroupContext clientGroupContext = new ClientGroupContext(tioClientHandler, aioListener, reconnConf);
		clientGroupContext.setHeartbeatTimeout(timeout);
		TioClient tioClient;
		try {
			tioClient = new TioClient(clientGroupContext);
			Node serverNode = new Node(serverIp, serverPort);
			clientChannelContext = tioClient.connect(serverNode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public <T> void handle(T t, HandlerCallback callback) {
		StoragePacket packet = new StoragePacket();
		Long seq = UniqueIdGenerator.generateId();
		tioClientHandler.callbackMap.put(seq, callback);
		packet.setSeq(seq);
		packet.setBody(JSON.toJSONString(t).getBytes());
		Tio.send(clientChannelContext, packet);

	}

}
