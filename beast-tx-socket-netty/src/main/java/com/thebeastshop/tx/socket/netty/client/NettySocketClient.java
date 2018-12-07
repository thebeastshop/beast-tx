/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 2018年10月12日
 */
package com.thebeastshop.tx.socket.netty.client;

import com.alibaba.fastjson.JSON;
import com.thebeastshop.tx.socket.client.SocketClient;
import com.thebeastshop.tx.socket.config.ClientConfig;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;

/**
 * 用netty通信框架实现客户端
 */
public class NettySocketClient implements SocketClient {

	private ClientConfig config;

	private Channel ch;

	public void init(EventLoopGroup eventLoopGroup) {
		try {
			final NettySocketClientHandler nettyClientHandler = new NettySocketClientHandler(this);
			nettyClientHandler.handler = config.getHandler();
			Bootstrap b = new Bootstrap();
			b.group(eventLoopGroup).channel(NioSocketChannel.class).option(ChannelOption.SO_KEEPALIVE, true).handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new ByteArrayDecoder());
					p.addLast(new ByteArrayEncoder());
					p.addLast(nettyClientHandler);
				}
			});

			// Start the connection attempt.
			ch = b.connect(config.getIp(), config.getPort()).addListener(new ConnectionListener(this)).channel();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public SocketClient initClient(ClientConfig config) {
		this.config = config;
		init(new NioEventLoopGroup());
		return this;
	}

	@Override
	public <T> void send(T t) {
		ch.writeAndFlush(JSON.toJSONString(t).getBytes());
	}
}
