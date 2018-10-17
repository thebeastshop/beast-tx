/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 2018年10月12日
 */
package com.thebeastshop.tx.storage.tio;

import org.tio.core.intf.Packet;

public class StoragePacket extends Packet {
	private static final long serialVersionUID = 1L;
	public static final int HEADER_LENGHT = 4;// 消息头的长度
	private long callbackSeq;
	private byte[] body;

	public long getCallbackSeq() {
		return callbackSeq;
	}

	public void setCallbackSeq(long callbackSeq) {
		this.callbackSeq = callbackSeq;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}
}
