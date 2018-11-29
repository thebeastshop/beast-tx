/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 2018年10月17日
 */
package com.thebeastshop.tx.network.config;

import com.thebeastshop.tx.network.server.NetworkServerHandler;

/**
 * 
 */
public class ServerConfig {

	private String ip = "127.0.0.1";

	private int port = 6789;

	private int timeout = 5000;

	private NetworkServerHandler handler;

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

	public NetworkServerHandler getHandler() {
		return handler;
	}

	public void setHandler(NetworkServerHandler handler) {
		this.handler = handler;
	}

}
