/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 
 */
package com.thebeastshop.tx;

import com.thebeastshop.tx.record.storage.RecordStorageClient;
import com.thebeastshop.tx.record.storage.RecordStorageClientProvider;
import com.thebeastshop.tx.vo.Record;

public class RecordStorageClientTest {

	public static void main(String[] args) {
		RecordStorageClient client = RecordStorageClientProvider.create();
		Record record = new Record();
		record.setTxId(1L);
		client.store(record);
	}

}
