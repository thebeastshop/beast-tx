/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 2018年10月12日
 */
package com.thebeastshop.tx;

import com.alibaba.fastjson.JSON;
import com.thebeastshop.tx.network.client.NetworkClient;
import com.thebeastshop.tx.network.client.NetworkClientHandler;
import com.thebeastshop.tx.network.client.NetworkClientProvider;
import com.thebeastshop.tx.network.config.ClientConfig;
import com.thebeastshop.tx.vo.Record;

public class NetworkClientTest {

	public static void main(String[] args) throws Exception {
		ClientConfig config = new ClientConfig();
		config.setHandler(new NetworkClientHandler() {
			@Override
			public void handle(byte[] dataBytes) {
				Record record = JSON.parseObject(dataBytes, Record.class);
				System.out.println("收到消息：" + JSON.toJSONString(record));
			}
		});
		NetworkClient client = NetworkClientProvider.create(config);
		Record record = new Record();
		record.setTxId(1L);
		client.send(record);
		
		System.in.read();
	}

}
