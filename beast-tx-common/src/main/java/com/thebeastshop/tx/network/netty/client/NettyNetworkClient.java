/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 2018年10月12日
 */
package com.thebeastshop.tx.network.netty.client;

import com.thebeastshop.tx.network.HasBytes;
import com.thebeastshop.tx.network.client.NetworkClient;
import com.thebeastshop.tx.network.config.ClientConfig;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
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
public class NettyNetworkClient implements NetworkClient {
	
	private Channel ch;

	@Override
	public NetworkClient initClient(ClientConfig config) {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			final NettyNetworkClientHandler nettyClientHandler = new NettyNetworkClientHandler();
			nettyClientHandler.handler = config.getHandler();
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new ByteArrayDecoder());
			        p.addLast(new ByteArrayEncoder());
					p.addLast(nettyClientHandler);
				}
			});

			// Start the connection attempt.
			ch = b.connect(config.getIp(), config.getPort()).sync().channel();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public void send(HasBytes t) {
		ch.writeAndFlush(t.toBytes());
	}

}
