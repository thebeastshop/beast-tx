/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 
 */
package com.thebeastshop.tx.record.storage.tio;

import java.util.List;

import org.springframework.stereotype.Component;
import org.tio.client.ClientChannelContext;
import org.tio.client.ClientGroupContext;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.client.intf.ClientAioHandler;
import org.tio.client.intf.ClientAioListener;
import org.tio.core.Node;
import org.tio.core.Tio;

import com.alibaba.fastjson.JSON;
import com.thebeastshop.tx.record.storage.RecordStorageClient;
import com.thebeastshop.tx.tio.RecordPacket;
import com.thebeastshop.tx.vo.Record;

/**
 * 用t-io通信框架实现记录存储客户端
 */
@Component
public class RecordStorageTioClient implements RecordStorageClient {

	private ClientChannelContext clientChannelContext;

	private String serverIp = null;

	private int serverPort = 6789;

	private int timeout = 5000;

	public RecordStorageTioClient() {
		super();
		ClientAioHandler tioClientHandler = new RecordClientAioHandler();
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
	public void store(Record record) {
		RecordPacket packet = new RecordPacket();
		packet.setBody(JSON.toJSONString(record).getBytes());
		Tio.send(clientChannelContext, packet);
	}

	@Override
	public void store(List<Record> records) {

	}

	@Override
	public List<Record> fetch(Long txId) {
		return null;
	}

}
