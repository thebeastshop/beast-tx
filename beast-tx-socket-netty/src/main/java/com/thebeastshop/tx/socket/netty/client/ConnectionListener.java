/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date Dec 7, 2018
 */
package com.thebeastshop.tx.socket.netty.client;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoop;

/**
 * 
 */
public class ConnectionListener implements ChannelFutureListener {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	private NettySocketClient client;

	public ConnectionListener(NettySocketClient client) {
		this.client = client;
	}

	@Override
	public void operationComplete(ChannelFuture channelFuture) throws Exception {
		if (!channelFuture.isSuccess()) {
			log.warn("Try Reconnect");
			final EventLoop loop = channelFuture.channel().eventLoop();
			loop.schedule(new Runnable() {
				@Override
				public void run() {
					client.init(loop);
				}
			}, 1L, TimeUnit.SECONDS);
		} else {
			log.warn("Reconnect Success");
		}
	}

}
