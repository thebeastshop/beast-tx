/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 
 */
package com.thebeastshop.tx.vo;

import com.alibaba.fastjson.JSON;
import com.thebeastshop.tx.socket.HasBytes;

/**
 * 
 */
public class Record implements HasBytes {

	private long txId;

	public long getTxId() {
		return txId;
	}

	public void setTxId(long txId) {
		this.txId = txId;
	}

	@Override
	public byte[] toBytes() {
		return JSON.toJSONString(this).getBytes();
	}

}
