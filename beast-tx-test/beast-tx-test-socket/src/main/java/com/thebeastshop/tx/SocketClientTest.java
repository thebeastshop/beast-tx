/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 2018年10月12日
 */
package com.thebeastshop.tx;

import com.alibaba.fastjson.JSON;
import com.thebeastshop.tx.socket.client.SocketClient;
import com.thebeastshop.tx.socket.client.SocketClientHandler;
import com.thebeastshop.tx.socket.client.SocketClientProvider;
import com.thebeastshop.tx.socket.config.ClientConfig;
import com.thebeastshop.tx.vo.MonitorVo;

public class SocketClientTest {

	public static void main(String[] args) throws Exception {
		ClientConfig config = new ClientConfig();
		config.setHandler(new SocketClientHandler() {
			@Override
			public void handle(byte[] dataBytes) {
				MonitorVo record = JSON.parseObject(dataBytes, MonitorVo.class);
				System.out.println("收到消息：" + JSON.toJSONString(record));
			}
		});
		SocketClient client = SocketClientProvider.create(config);
		MonitorVo record = new MonitorVo();
		record.setTxId(133L);
		int second = 100;
		while (second-- > 0) {
			System.out.println("second: " + second);
			client.send(record);
			Thread.sleep(1000);
		}

	}

}
