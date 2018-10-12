/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 
 */
package com.thebeastshop.tx.tio;

import org.tio.core.intf.Packet;

public class RecordPacket extends Packet {
	private static final long serialVersionUID = -172060606924066412L;
	public static final int HEADER_LENGHT = 4;//消息头的长度
	public static final String CHARSET = "utf-8";
	private byte[] body;

	/**
	 * @return the body
	 */
	public byte[] getBody() {
		return body;
	}

	/**
	 * @param body the body to set
	 */
	public void setBody(byte[] body) {
		this.body = body;
	}
}
