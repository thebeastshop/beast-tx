/**
 * <p>Title: beast-tx</p >
 * <p>Description: 分布式事务框架，基于TCC事务的事务框架监控跟踪平台</p >
 * @author Paul.Xiong
 * @email xiongleipaul@gmail.com
 * @Date 2018年10月17日
 */
package com.thebeastshop.tx.socket.netty.server;

import org.apache.commons.lang3.ArrayUtils;

import com.thebeastshop.tx.socket.HasBytes;
import com.thebeastshop.tx.socket.server.SocketServerHandler;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * 
 */
@Sharable
public class NettySocketServerHandler extends SimpleChannelInboundHandler<byte[]>{
	
	public SocketServerHandler handler;

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
