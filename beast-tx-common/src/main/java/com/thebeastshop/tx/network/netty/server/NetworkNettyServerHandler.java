/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于本地事务表模型，支持最终一致事务，TCC事务的事务框架平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 2018年10月17日
 */
package com.thebeastshop.tx.network.netty.server;

import org.apache.commons.lang3.ArrayUtils;

import com.thebeastshop.tx.network.HasBytes;
import com.thebeastshop.tx.network.server.NetworkServerHandler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 
 */
@Sharable
public class NetworkNettyServerHandler extends SimpleChannelInboundHandler<byte[]>{
	
	public NetworkServerHandler handler;

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, byte[] msg) throws Exception {
		if (ArrayUtils.isNotEmpty(msg) && handler != null) {
			HasBytes reply = handler.receive(msg);
			if (reply != null) {
				ctx.writeAndFlush(reply.toBytes());
			}
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

}
