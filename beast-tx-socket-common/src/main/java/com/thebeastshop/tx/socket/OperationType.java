/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 2018年10月12日
 */
package com.thebeastshop.tx.socket;

/**
 * 操作类型
 */
public enum OperationType {
	STORE((byte) 1, "存储操作"), 
	FETCH((byte) 2, "获取操作");

	private byte code;

	private String name;

	private OperationType(byte code, String name) {
		this.code = code;
		this.name = name;
	}

	public byte getCode() {
		return code;
	}

	public void setCode(byte code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static OperationType valueOf(byte code) {
		for (OperationType e : OperationType.values()) {
			if (e.getCode() == code) {
				return e;
			}
		}
		return null;
	}

}
