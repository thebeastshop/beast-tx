/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 2018年10月12日
 */
package com.thebeastshop.tx;

import com.alibaba.fastjson.JSON;
import com.thebeastshop.tx.storage.client.HandlerCallback;
import com.thebeastshop.tx.storage.client.StorageClient;
import com.thebeastshop.tx.storage.client.StorageClientProvider;
import com.thebeastshop.tx.vo.Record;

public class StorageClientTest {

	public static void main(String[] args) {
		StorageClient client = StorageClientProvider.create();
		Record record = new Record();
		record.setTxId(1L);
		client.handle(record, new HandlerCallback() {
			@Override
			public void doCallback(byte[] dataBytes) {
				Record record = JSON.parseObject(dataBytes, Record.class);
				System.out.println("收到消息：" + JSON.toJSONString(record));
			}
		});
	}

}
