/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 
 */
package com.thebeastshop.tx.record.storage;

import java.util.List;

import com.thebeastshop.tx.vo.Record;

/**
 * 记录存储客户端
 */
public interface RecordStorageClient {

	void store(Record record);

	void store(List<Record> records);

	List<Record> fetch(Long txId);

}
