/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 2018年10月17日
 */
package com.thebeastshop.tx.socket.config;

import com.thebeastshop.tx.socket.client.SocketClientHandler;

/**
 * 
 */
public class ClientConfig {

	private String ip = "127.0.0.1";

	private int port = 6789;

	private int timeout = 5000;

	private SocketClientHandler handler;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public SocketClientHandler getHandler() {
		return handler;
	}

	public void setHandler(SocketClientHandler handler) {
		this.handler = handler;
	}

}
